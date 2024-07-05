package com.petplate.petplate.petdailymeal.dto.response;

import com.petplate.petplate.petdailymeal.domain.entity.DailyFeed;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReadDailyFeedResponseDto {
    private Long dailyFeedId;
    private String name;
    private double serving;
    private double kcal;
    private double carbonHydrate;
    private double protein;
    private double fat;
    private double calcium;
    private double phosphorus;
    private double vitaminA;
    private double vitaminD;
    private double vitaminE;

    public static ReadDailyFeedResponseDto from(DailyFeed dailyFeed) {
        ReadDailyFeedResponseDto response
                = new ReadDailyFeedResponseDto();

        response.dailyFeedId = dailyFeed.getId();
        response.name = dailyFeed.getName();
        response.serving = dailyFeed.getServing();
        response.kcal = dailyFeed.getKcal();
        response.carbonHydrate = dailyFeed.getNutrient().getCarbonHydrate();
        response.protein = dailyFeed.getNutrient().getProtein();
        response.fat = dailyFeed.getNutrient().getFat();
        response.calcium = dailyFeed.getNutrient().getCalcium();
        response.phosphorus = dailyFeed.getNutrient().getPhosphorus();
        response.vitaminA = dailyFeed.getNutrient().getVitamin().getVitaminA();
        response.vitaminD = dailyFeed.getNutrient().getVitamin().getVitaminD();
        response.vitaminE = dailyFeed.getNutrient().getVitamin().getVitaminE();

        return response;
    }
}
