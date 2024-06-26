package com.petplate.petplate.auth.oauth.handler;

import com.petplate.petplate.auth.jwt.TokenProvider;
import com.petplate.petplate.auth.oauth.CustomOAuth2User;
import com.petplate.petplate.auth.oauth.Dto.TokenDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final RedisTemplate<String,String> redisTemplate;
    private final static String authorization = "Authorization";
    private final static String refreshToken = "refreshToken";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        try{

            CustomOAuth2User oAuth2User=((CustomOAuth2User) authentication.getPrincipal());

            TokenDto tokenDto=tokenProvider.createTokenByOAuth(oAuth2User);//OAuth2로 새로운 access,refreshToken 생성


            redisTemplate.opsForValue().set(oAuth2User.getUsername(),tokenDto.getRefreshToken(),tokenDto.getRefreshTokenValidationTime(), TimeUnit.MILLISECONDS);
            // Dto 객체를 JSON으로 변환하여 응답으로 전송
            response.setHeader(authorization,tokenDto.getAccessToken());
            response.setHeader(refreshToken,tokenDto.getRefreshToken());



        }catch (Exception e){
            throw e;
        }


    }

    private String makeUrl(){
        return "localhost:8080";
    }


}

