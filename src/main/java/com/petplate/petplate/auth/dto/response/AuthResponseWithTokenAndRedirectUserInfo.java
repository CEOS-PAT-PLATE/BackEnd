package com.petplate.petplate.auth.dto.response;

import com.petplate.petplate.auth.oauth.Dto.TokenDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthResponseWithTokenAndRedirectUserInfo {

    private TokenDto tokenDto;
    private UserEnrollResponseDto userEnrollResponseDto;

    @Builder
    public AuthResponseWithTokenAndRedirectUserInfo(TokenDto tokenDto,
            UserEnrollResponseDto userEnrollResponseDto) {
        this.tokenDto = tokenDto;
        this.userEnrollResponseDto = userEnrollResponseDto;
    }
}
