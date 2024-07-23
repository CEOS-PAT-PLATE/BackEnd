package com.petplate.petplate.petdailymeal.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateDailyBookMarkedPackagedSnackRequestDto {
    @NotNull(message = "포장간식이 입력되지 않았습니다")
    private Long bookMarkedPackagedSnackId;
}
