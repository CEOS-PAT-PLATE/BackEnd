package com.petplate.petplate.auth.oauth.handler;

import static com.petplate.petplate.auth.oauth.cookie.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

import com.petplate.petplate.auth.jwt.TokenProvider;
import com.petplate.petplate.auth.oauth.CustomOAuth2User;
import com.petplate.petplate.auth.oauth.Dto.TokenDto;
import com.petplate.petplate.auth.oauth.cookie.HttpCookieOAuth2AuthorizationRequestRepository;
import com.petplate.petplate.common.utils.CookieUtils;
import com.petplate.petplate.pet.domain.entity.Pet;
import com.petplate.petplate.pet.repository.PetRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
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
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final PetRepository petRepository;

    private final static String petEnrollUrl = "input-data1";
    private final static String foodEnrollUrl = "input-data2";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        try{

            CustomOAuth2User oAuth2User=((CustomOAuth2User) authentication.getPrincipal());

            TokenDto tokenDto=tokenProvider.createTokenByOAuth(oAuth2User);//OAuth2로 새로운 access,refreshToken 생성


            redisTemplate.opsForValue().set(oAuth2User.getUsername(),tokenDto.getRefreshToken(),tokenDto.getRefreshTokenValidationTime(), TimeUnit.MILLISECONDS);
            // Dto 객체를 JSON으로 변환하여 응답으로 전송

            response.setHeader(authorization,tokenDto.getAccessToken());
            response.setHeader(refreshToken,tokenDto.getRefreshToken());

            String redirectUrl = generateBaseUrl(request, response)+getSubUrl(oAuth2User.getUsername());
            System.out.println(redirectUrl);

            response.sendRedirect(redirectUrl);



        }catch (Exception e){
            throw e;
        }


    }

    private String makeUrl(){
        return "localhost:8080";
    }

    private String generateBaseUrl(HttpServletRequest request, HttpServletResponse response) {

        String targetUrl = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue).orElse("");
        clearAuthenticationAttributes(request, response);

        return targetUrl;

    }

    private void clearAuthenticationAttributes(HttpServletRequest request,
            HttpServletResponse response) {

        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request,
                response);
    }

    private String getSubUrl(String username){

        /*if(petRepository.existsByOwnerUsername(username)){

            return foodEnrollUrl;
        }

        return petEnrollUrl;*/

        return "/sign-up";
    }


}

