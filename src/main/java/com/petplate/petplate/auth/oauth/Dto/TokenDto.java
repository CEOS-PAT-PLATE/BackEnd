package com.petplate.petplate.auth.oauth.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class TokenDto {
    private String accessToken;
    private String refreshToken;
    private String type;
    private Long accessTokenValidationTime;
    private Long refreshTokenValidationTime;
}