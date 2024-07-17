package com.petplate.petplate.auth.oauth.service;




import com.petplate.petplate.auth.jwt.TokenProvider;
import com.petplate.petplate.auth.oauth.CustomOAuth2User;
import com.petplate.petplate.auth.oauth.Dto.SocialLoginProfileResponseDto;
import com.petplate.petplate.auth.oauth.Dto.SocialLoginReIssueResponseDto;
import com.petplate.petplate.auth.oauth.Dto.SocialLoginTokenRequestResponseDto;
import com.petplate.petplate.auth.oauth.Dto.TokenDto;
import com.petplate.petplate.common.response.error.ErrorCode;
import com.petplate.petplate.common.response.error.exception.BadRequestException;
import com.petplate.petplate.common.response.error.exception.InternalServerErrorException;
import com.petplate.petplate.common.response.error.exception.NotFoundException;
import com.petplate.petplate.user.domain.Role;
import com.petplate.petplate.user.domain.SocialType;
import com.petplate.petplate.user.domain.entity.User;
import com.petplate.petplate.user.repository.UserRepository;
import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.h2.engine.UserBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocialLoginTokenUtil {



    private final WebClient webClient;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String naverClientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String naverClientSecret;

    private final UserRepository userRepository;

    private final TokenProvider tokenProvider;

    private final static String success= "success";


    @Transactional
    public void saveSocialLoginRefreshToken(String username,String socialLoginRefreshToken){

        User findUser = findUserByUsername(username);

        findUser.changeSocialLoginRefreshToken(socialLoginRefreshToken);
    }


    public SocialLoginProfileResponseDto getProfileByCode(final String code){

        SocialLoginTokenRequestResponseDto socialLoginTokenRequestResponseDto = getSocialLoginTokenIssue(code);

        return  getSocialLoginProfile(socialLoginTokenRequestResponseDto.getAccess_token(),
                socialLoginTokenRequestResponseDto.getRefresh_token());

    }


    private SocialLoginReIssueResponseDto getNewSocialLoginAccessToken(String refreshToken){
        return webClient.get()
                .uri("?grant_type=refresh_token&client_id="+naverClientId+"&client_secret="+naverClientSecret+"&refresh_token="+refreshToken)
                .retrieve()
                .bodyToMono(SocialLoginReIssueResponseDto.class)
                .block();
    }

    public SocialLoginTokenRequestResponseDto getSocialLoginTokenIssue(String code){

        SocialLoginTokenRequestResponseDto socialLoginTokenRequestResponseDto= webClient.get()
                .uri("?grant_type=authorization_code&client_id="+naverClientId+"&client_secret="+naverClientSecret+"&code="+code)
                .retrieve()
                .bodyToMono(SocialLoginTokenRequestResponseDto.class)
                .block();

        if(socialLoginTokenRequestResponseDto.getError()!=null){
            throw new BadRequestException(ErrorCode.SOCIAL_LOGIN_CODE);
        }

        return socialLoginTokenRequestResponseDto;
    }

    private SocialLoginProfileResponseDto getSocialLoginProfile(String accessToken,String refreshToken) {

        SocialLoginProfileResponseDto socialLoginProfileResponseDto =  webClient.get()
                .uri("https://openapi.naver.com/v1/nid/me")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(SocialLoginProfileResponseDto.class)
                .block();

        if(!socialLoginProfileResponseDto.getMessage().equals(success)){
            throw new InternalServerErrorException(ErrorCode.SOCIAL_LOGIN_CODE);
        }

        socialLoginProfileResponseDto.initWithRefreshToken(refreshToken);

        return socialLoginProfileResponseDto;
    }


    public void unlinkNaver(String username){

        User findUser = findUserByUsername(username);

        SocialLoginReIssueResponseDto tokenReIssueResponseDto = getNewSocialLoginAccessToken(
                findUser.getSocialLoginRefreshToken());

        System.out.println(tokenReIssueResponseDto.getAccess_token());

        webClient.post()
                .uri("?grant_type=delete&client_id="+naverClientId+"&client_secret="+naverClientSecret+"&access_token="+tokenReIssueResponseDto.getAccess_token())
                .retrieve()
                .toBodilessEntity()
                .block();

    }

    private User findUserByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow(()->new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }




}

