package com.petplate.petplate.petdailymeal.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateDailyBookMarkedRawRequestDto {
    @NotBlank(message = "즐겨찾기한 자연식이 입력되지 않았습니다")
    private Long dailyRawId;
}
