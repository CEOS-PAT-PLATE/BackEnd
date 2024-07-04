package com.petplate.petplate.petdailymeal.dto.response;

import com.petplate.petplate.petdailymeal.domain.entity.DailyBookMarkedRaw;
import com.petplate.petplate.petfood.domain.entity.BookMarkedRaw;
import com.petplate.petplate.petfood.dto.response.ReadBookMarkedRawResponseDto;

public class ReadDailyBookMarkedRawResponseDto {
    private Long dailyBookMarkedRawId;
    private String name;
    private String description;
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

    public static ReadDailyBookMarkedRawResponseDto from(DailyBookMarkedRaw dailybookMarkedRaw) {
        ReadDailyBookMarkedRawResponseDto response
                = new ReadDailyBookMarkedRawResponseDto();

        response.dailyBookMarkedRawId = dailybookMarkedRaw.getId();
        response.name = dailybookMarkedRaw.getBookMarkedRaw().getRaw().getName();
        response.description = dailybookMarkedRaw.getBookMarkedRaw().getDescription();
        response.serving = dailybookMarkedRaw.getBookMarkedRaw().getServing();
        response.kcal = dailybookMarkedRaw.getBookMarkedRaw().getKcal();
        response.carbonHydrate = dailybookMarkedRaw.getBookMarkedRaw().getNutrient().getCarbonHydrate();
        response.protein = dailybookMarkedRaw.getBookMarkedRaw().getNutrient().getProtein();
        response.fat = dailybookMarkedRaw.getBookMarkedRaw().getNutrient().getFat();
        response.calcium = dailybookMarkedRaw.getBookMarkedRaw().getNutrient().getCalcium();
        response.phosphorus = dailybookMarkedRaw.getBookMarkedRaw().getNutrient().getPhosphorus();
        response.vitaminA = dailybookMarkedRaw.getBookMarkedRaw().getNutrient().getVitamin().getVitaminA();
        response.vitaminD = dailybookMarkedRaw.getBookMarkedRaw().getNutrient().getVitamin().getVitaminD();
        response.vitaminE = dailybookMarkedRaw.getBookMarkedRaw().getNutrient().getVitamin().getVitaminE();

        return response;
    }
}
