package com.petplate.petplate.auth.oauth.service;


import com.petplate.petplate.auth.dto.response.TokenReIssueResponseDto;
import com.petplate.petplate.auth.oauth.Dto.SocialLoginReIssueResponseDto;
import com.petplate.petplate.common.response.error.ErrorCode;
import com.petplate.petplate.common.response.error.exception.BadRequestException;
import java.net.URI;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisSocialLoginTokenUtil {

    private final RedisTemplate redisTemplate;
    private static final String SOCIAL_LOGIN_REFRESH_TOKEN = "SocialLoginRefreshToken";
    private static final String SOCIAL_LOGIN_ACCESS_TOKEN = "SocialLoginAccessToken";
    private static final Long SOCIAL_LOGIN_ACCESS_TOKEN_EXPIRE = 3600L;

    private final WebClient webClient;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String naverClientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String naverClientSecret;


    public void saveSocialLoginRefreshToken(String username,String socialLoginRefreshToken){
        redisTemplate.opsForValue().set(SOCIAL_LOGIN_REFRESH_TOKEN+username,socialLoginRefreshToken);
    }


    private SocialLoginReIssueResponseDto getNewSocialLoginAccessToken(String refreshToken){
        return webClient.get()
                .uri("?grant_type=refresh_token&client_id="+naverClientId+"&client_secret="+naverClientSecret+"&refresh_token="+refreshToken)
                .retrieve()
                .bodyToMono(SocialLoginReIssueResponseDto.class)
                .block();
    }

    public void unlinkNaver(String username){

        String  existingRefreshToken = (String) redisTemplate.opsForValue().get(SOCIAL_LOGIN_REFRESH_TOKEN+username);



        SocialLoginReIssueResponseDto tokenReIssueResponseDto = getNewSocialLoginAccessToken(existingRefreshToken);

        System.out.println(tokenReIssueResponseDto.getAccess_token());

        webClient.post()
                .uri("?grant_type=delete&client_id="+naverClientId+"&client_secret="+naverClientSecret+"&access_token="+tokenReIssueResponseDto.getAccess_token())
                .retrieve()
                .toBodilessEntity()
                .block();

    }


}

