package com.petplate.petplate.auth.oauth.service;


import com.petplate.petplate.auth.oauth.Dto.SocialLoginReIssueResponseDto;
import com.petplate.petplate.common.response.error.ErrorCode;
import com.petplate.petplate.common.response.error.exception.NotFoundException;
import com.petplate.petplate.user.domain.entity.User;
import com.petplate.petplate.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
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


    @Transactional
    public void saveSocialLoginRefreshToken(String username,String socialLoginRefreshToken){

        User findUser = findUserByUsername(username);

        findUser.changeSocialLoginRefreshToken(socialLoginRefreshToken);
    }


    private SocialLoginReIssueResponseDto getNewSocialLoginAccessToken(String refreshToken){
        return webClient.get()
                .uri("?grant_type=refresh_token&client_id="+naverClientId+"&client_secret="+naverClientSecret+"&refresh_token="+refreshToken)
                .retrieve()
                .bodyToMono(SocialLoginReIssueResponseDto.class)
                .block();
    }

    public void unlinkNaver(String username){

        User findUser = findUserByUsername(username);

        SocialLoginReIssueResponseDto tokenReIssueResponseDto = getNewSocialLoginAccessToken(
                findUser.getSocialLoginRefreshToken());

        //System.out.println(tokenReIssueResponseDto.getAccess_token());

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

