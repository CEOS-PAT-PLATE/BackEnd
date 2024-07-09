package com.petplate.petplate.petdailymeal.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateDailyBookMarkedFeedRequestDto {
    @NotBlank(message = "즐겨찾기한 사료가 입력되지 않았습니다")
    private Long bookMarkedFeedId;
}
