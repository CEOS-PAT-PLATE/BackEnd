package com.petplate.petplate.petdailymeal.dto.response;

import com.petplate.petplate.petdailymeal.domain.entity.DailyBookMarkedPackagedSnack;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReadDailyBookMarkedPackagedSnackResponseDto {
    private Long dailyBookMarkedPackagedSnackId;
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

    public static ReadDailyBookMarkedPackagedSnackResponseDto from(DailyBookMarkedPackagedSnack dailyBookMarkedPackagedSnack) {
        ReadDailyBookMarkedPackagedSnackResponseDto response
                = new ReadDailyBookMarkedPackagedSnackResponseDto();

        response.dailyBookMarkedPackagedSnackId = dailyBookMarkedPackagedSnack.getId();
        response.name = dailyBookMarkedPackagedSnack.getBookMarkedPackagedSnack().getName();
        response.serving = dailyBookMarkedPackagedSnack.getBookMarkedPackagedSnack().getServing();
        response.kcal = dailyBookMarkedPackagedSnack.getBookMarkedPackagedSnack().getKcal();
        response.carbonHydrate = dailyBookMarkedPackagedSnack.getBookMarkedPackagedSnack().getNutrient().getCarbonHydrate();
        response.protein = dailyBookMarkedPackagedSnack.getBookMarkedPackagedSnack().getNutrient().getProtein();
        response.fat = dailyBookMarkedPackagedSnack.getBookMarkedPackagedSnack().getNutrient().getFat();
        response.calcium = dailyBookMarkedPackagedSnack.getBookMarkedPackagedSnack().getNutrient().getCalcium();
        response.phosphorus = dailyBookMarkedPackagedSnack.getBookMarkedPackagedSnack().getNutrient().getPhosphorus();
        response.vitaminA = dailyBookMarkedPackagedSnack.getBookMarkedPackagedSnack().getNutrient().getVitamin().getVitaminA();
        response.vitaminD = dailyBookMarkedPackagedSnack.getBookMarkedPackagedSnack().getNutrient().getVitamin().getVitaminD();
        response.vitaminE = dailyBookMarkedPackagedSnack.getBookMarkedPackagedSnack().getNutrient().getVitamin().getVitaminE();

        return response;
    }
}