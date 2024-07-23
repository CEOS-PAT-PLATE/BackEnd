package com.petplate.petplate.auth.service;

import com.petplate.petplate.auth.dto.response.AuthResponseWithTokenAndRedirectUserInfo;
import com.petplate.petplate.auth.dto.response.UserEnrollResponseDto;
import com.petplate.petplate.auth.jwt.TokenProvider;
import com.petplate.petplate.auth.oauth.Dto.SocialInfoWithTokenDto;
import com.petplate.petplate.auth.oauth.Dto.SocialLoginProfileResponseDto;
import com.petplate.petplate.auth.oauth.Dto.SocialLoginTokenRequestResponseDto;
import com.petplate.petplate.auth.oauth.Dto.TokenDto;
import com.petplate.petplate.auth.oauth.service.SocialLoginTokenUtil;
import com.petplate.petplate.common.response.error.ErrorCode;
import com.petplate.petplate.common.response.error.exception.BadRequestException;
import com.petplate.petplate.pet.repository.PetRepository;
import com.petplate.petplate.user.domain.Role;
import com.petplate.petplate.user.domain.SocialType;
import com.petplate.petplate.user.domain.entity.User;
import com.petplate.petplate.user.repository.UserRepository;
import java.util.UUID;
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
    private final SocialLoginTokenUtil socialLoginTokenUtil;
    private final UserRepository userRepository;
    private final PetRepository petRepository;

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

    @Transactional
    public AuthResponseWithTokenAndRedirectUserInfo getTokenByCode(final String code){

        SocialInfoWithTokenDto socialInfoWithTokenDto = socialLoginTokenUtil.getSocialInfoAndTokenByCode(code);

        User createdMember = userRepository.findBySocialTypeAndUsername(SocialType.NAVER,socialInfoWithTokenDto.getEmail())
                .orElseGet(()->{
                    User savedUser = User.builder()
                            .name(socialInfoWithTokenDto.getName())
                            .role(Role.GENERAL)
                            .socialType(SocialType.NAVER)
                            .username(socialInfoWithTokenDto.getEmail())
                            .activated(false)
                            .isReceiveAd(false)
                            .password(UUID.randomUUID()+"password")
                            .phoneNumber(null)
                            .build();

                    return  userRepository.save(savedUser);

                });

        createdMember.changeSocialLoginRefreshToken(socialInfoWithTokenDto.getSocialLoginRefreshToken());
        socialLoginTokenUtil.saveSocialLoginAccessToken(createdMember.getUsername(),socialInfoWithTokenDto.getSocialLoginAccessToken());

        TokenDto tokenDto = tokenProvider.createTokenByUserProperty(createdMember.getUsername(),createdMember.getRole().name());
        saveRefreshTokenAtRedis(createdMember.getUsername(),tokenDto);

        return AuthResponseWithTokenAndRedirectUserInfo.builder()
                .tokenDto(tokenDto)
                .userEnrollResponseDto(getBasicUserInfoForRedirect(createdMember.getUsername()))
                .build();

    }

    public UserEnrollResponseDto getBasicUserInfoForRedirect(String username){

        return UserEnrollResponseDto.builder()
                .enrollPet(petRepository.existsByOwnerUsername(username))
                .build();
    }

}
