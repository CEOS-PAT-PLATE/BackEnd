package com.petplate.petplate.petdailymeal.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AddRawDailyMealRequestDto {
    private Long dailyMealId;
    private Long rawId;
    private double serving;
}
