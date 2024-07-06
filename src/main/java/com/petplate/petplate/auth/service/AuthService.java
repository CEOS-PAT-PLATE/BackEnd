package com.petplate.petplate.auth.service;

import com.petplate.petplate.auth.dto.response.TokenReIssueResponseDto;
import com.petplate.petplate.auth.jwt.TokenProvider;
import com.petplate.petplate.auth.oauth.Dto.TokenDto;
import com.petplate.petplate.auth.oauth.service.RedisSocialLoginTokenUtil;
import com.petplate.petplate.common.response.error.ErrorCode;
import com.petplate.petplate.common.response.error.exception.BadRequestException;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {


    private final TokenProvider tokenProvider;
    private final RedisTemplate redisTemplate;
    private final RedisSocialLoginTokenUtil redisSocialLoginTokenUtil;

    @Transactional
    public void logout(final String accessToken){

        if (!tokenProvider.validateToken(accessToken)){
            throw new BadRequestException(ErrorCode.ACCESS_TOKEN_NOT_MATCH);
        }

        Authentication authentication = tokenProvider.getAuthentication(accessToken);

        if (redisTemplate.opsForValue().get(authentication.getName())!=null){
            redisTemplate.delete(authentication.getName());
        }


        Long expiration = tokenProvider.getExpiration(accessToken);
        redisTemplate.opsForValue().set(accessToken,"logout",expiration, TimeUnit.MILLISECONDS);

    }

    @Transactional
    public TokenDto reIssue(final String accessToken, final String refreshToken){

        Authentication authentication= tokenProvider.getAuthentication(accessToken);

        if(!redisTemplate.opsForValue().get(authentication.getName()).equals(refreshToken)){
            throw new BadRequestException(ErrorCode.REFRESH_TOKEN_NOT_MATCH);
        }

        //자체 토큰
        TokenDto tokenDto=tokenProvider.createToken(authentication);
        saveRefreshTokenAtRedis(authentication.getName(),tokenDto);


        return tokenDto;
    }

    private void saveRefreshTokenAtRedis(String key, TokenDto tokenDto){
        redisTemplate.opsForValue().set(key,tokenDto.getRefreshToken(),tokenDto.getRefreshTokenValidationTime(),TimeUnit.MILLISECONDS);
    }






}
