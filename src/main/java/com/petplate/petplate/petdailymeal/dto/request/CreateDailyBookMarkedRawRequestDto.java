package com.petplate.petplate.petdailymeal.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateDailyBookMarkedRawRequestDto {
    @NotNull(message = "즐겨찾기한 자연식이 입력되지 않았습니다")
    private Long bookMarkedRawId;
}
