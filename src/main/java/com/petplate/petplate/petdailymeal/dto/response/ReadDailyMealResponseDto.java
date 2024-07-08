package com.petplate.petplate.petdailymeal.dto.response;

import com.petplate.petplate.petdailymeal.domain.entity.DailyMeal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ReadDailyMealResponseDto {
    private Long dailyMealId;
    private LocalDate date;

    public static ReadDailyMealResponseDto from(DailyMeal dailyMeal) {
        ReadDailyMealResponseDto responseDto = new ReadDailyMealResponseDto();
        responseDto.dailyMealId = dailyMeal.getId();
        responseDto.date = dailyMeal.getCreatedAt().toLocalDate();

        return responseDto;
    }
}
