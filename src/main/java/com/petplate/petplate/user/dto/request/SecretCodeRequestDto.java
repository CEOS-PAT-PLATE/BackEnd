package com.petplate.petplate.user.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SecretCodeRequestDto {

    private String secretCode;

    @Builder
    public SecretCodeRequestDto(String secretCode){
        this.secretCode = secretCode;
    }

}
