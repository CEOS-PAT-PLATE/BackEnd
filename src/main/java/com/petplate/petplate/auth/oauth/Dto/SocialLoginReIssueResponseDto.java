package com.petplate.petplate.auth.oauth.Dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialLoginReIssueResponseDto {

    private String access_token;
    private String token_type;
    private String expires_in;



}
