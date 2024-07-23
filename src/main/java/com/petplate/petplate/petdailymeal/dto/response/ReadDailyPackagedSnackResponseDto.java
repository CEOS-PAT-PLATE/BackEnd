package com.petplate.petplate.petdailymeal.dto.response;

import com.petplate.petplate.petdailymeal.domain.entity.DailyPackagedSnack;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class ReadDailyPackagedSnackResponseDto {
    private Long dailyPackagedSnackId;
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

    public static ReadDailyPackagedSnackResponseDto from(DailyPackagedSnack dailyPackagedSnack) {
        ReadDailyPackagedSnackResponseDto response
                = new ReadDailyPackagedSnackResponseDto();

        response.dailyPackagedSnackId = dailyPackagedSnack.getId();
        response.name = dailyPackagedSnack.getName();
        response.serving = dailyPackagedSnack.getServing();
        response.kcal = dailyPackagedSnack.getKcal();
        response.carbonHydrate = dailyPackagedSnack.getNutrient().getCarbonHydrate();
        response.protein = dailyPackagedSnack.getNutrient().getProtein();
        response.fat = dailyPackagedSnack.getNutrient().getFat();
        response.calcium = dailyPackagedSnack.getNutrient().getCalcium();
        response.phosphorus = dailyPackagedSnack.getNutrient().getPhosphorus();
        response.vitaminA = dailyPackagedSnack.getNutrient().getVitamin().getVitaminA();
        response.vitaminD = dailyPackagedSnack.getNutrient().getVitamin().getVitaminD();
        response.vitaminE = dailyPackagedSnack.getNutrient().getVitamin().getVitaminE();

        return response;
    }

}
