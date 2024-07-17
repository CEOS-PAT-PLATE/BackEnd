package com.petplate.petplate.auth.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEnrollResponseDto {

    private boolean enrollPet;

    @Builder
    public UserEnrollResponseDto(boolean enrollPet) {
        this.enrollPet = enrollPet;
    }
}
