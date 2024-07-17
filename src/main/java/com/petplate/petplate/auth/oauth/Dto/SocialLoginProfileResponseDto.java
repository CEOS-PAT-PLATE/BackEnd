package com.petplate.petplate.auth.oauth.Dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialLoginProfileResponseDto {

    private String resultCode;
    private String message;
    private NaverResponse response;

    private String refreshToken;

    public void initWithRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }


}
