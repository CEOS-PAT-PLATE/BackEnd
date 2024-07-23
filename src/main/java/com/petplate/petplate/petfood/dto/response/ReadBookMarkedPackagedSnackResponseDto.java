package com.petplate.petplate.petfood.dto.response;

import com.petplate.petplate.petfood.domain.entity.BookMarkedPackagedSnack;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class ReadBookMarkedPackagedSnackResponseDto {
    private Long bookMarkedPackagedSnackId;
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

    public static ReadBookMarkedPackagedSnackResponseDto from(BookMarkedPackagedSnack bookMarkedPackagedSnack) {
        ReadBookMarkedPackagedSnackResponseDto response
                = new ReadBookMarkedPackagedSnackResponseDto();

        response.bookMarkedPackagedSnackId = bookMarkedPackagedSnack.getId();
        response.name = bookMarkedPackagedSnack.getName();
        response.serving = bookMarkedPackagedSnack.getServing();
        response.kcal = bookMarkedPackagedSnack.getKcal();
        response.carbonHydrate = bookMarkedPackagedSnack.getNutrient().getCarbonHydrate();
        response.protein = bookMarkedPackagedSnack.getNutrient().getProtein();
        response.fat = bookMarkedPackagedSnack.getNutrient().getFat();
        response.calcium = bookMarkedPackagedSnack.getNutrient().getCalcium();
        response.phosphorus = bookMarkedPackagedSnack.getNutrient().getPhosphorus();
        response.vitaminA = bookMarkedPackagedSnack.getNutrient().getVitamin().getVitaminA();
        response.vitaminD = bookMarkedPackagedSnack.getNutrient().getVitamin().getVitaminD();
        response.vitaminE = bookMarkedPackagedSnack.getNutrient().getVitamin().getVitaminE();

        return response;
    }
}
