package com.petplate.petplate.petdailymeal.dto.response;

import com.petplate.petplate.petdailymeal.domain.entity.RawDailyMeal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReadRawDailyMealResponseDto {
    private Long id;
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

    public static ReadRawDailyMealResponseDto from(RawDailyMeal rawDailyMeal) {
        ReadRawDailyMealResponseDto response
                = new ReadRawDailyMealResponseDto();

        response.id = rawDailyMeal.getId();
        response.name = rawDailyMeal.getRaw().getName();
        response.serving = rawDailyMeal.getServing();
        response.kcal = rawDailyMeal.getKcal();
        response.carbonHydrate = rawDailyMeal.getNutrient().getCarbonHydrate();
        response.protein = rawDailyMeal.getNutrient().getProtein();
        response.fat = rawDailyMeal.getNutrient().getFat();
        response.calcium = rawDailyMeal.getNutrient().getCalcium();
        response.phosphorus = rawDailyMeal.getNutrient().getPhosphorus();
        response.vitaminA = rawDailyMeal.getNutrient().getVitamin().getVitaminA();
        response.vitaminD = rawDailyMeal.getNutrient().getVitamin().getVitaminD();
        response.vitaminE = rawDailyMeal.getNutrient().getVitamin().getVitaminE();

        return response;
    }
}
