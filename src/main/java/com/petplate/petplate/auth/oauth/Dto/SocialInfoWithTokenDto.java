package com.petplate.petplate.auth.oauth.Dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialInfoWithTokenDto {

    private String socialLoginAccessToken;
    private String socialLoginRefreshToken;
    private String email;
    private String name;


    @Builder
    public SocialInfoWithTokenDto(final String socialLoginAccessToken, final String socialLoginRefreshToken,
            final String email,final String name) {
        this.socialLoginAccessToken = socialLoginAccessToken;
        this.socialLoginRefreshToken = socialLoginRefreshToken;
        this.email = email;
        this.name = name;
    }
}
