package com.petplate.petplate.petfood.dto.response;

import com.petplate.petplate.petfood.domain.entity.BookMarkedRaw;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReadBookMarkedRawResponseDto {
    private Long bookMarkedRawId;
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

    public static ReadBookMarkedRawResponseDto from(BookMarkedRaw bookMarkedRaw) {
        ReadBookMarkedRawResponseDto response
                = new ReadBookMarkedRawResponseDto();

        response.bookMarkedRawId = bookMarkedRaw.getId();
        response.name = bookMarkedRaw.getRaw()!=null?bookMarkedRaw.getRaw().getName():"존재하지 않는 음식입니다";
        response.description = bookMarkedRaw.getRaw() != null ? bookMarkedRaw.getRaw().getDescription() : null;
        response.serving = bookMarkedRaw.getServing();
        response.kcal = bookMarkedRaw.getKcal();
        response.carbonHydrate = bookMarkedRaw.getNutrient().getCarbonHydrate();
        response.protein = bookMarkedRaw.getNutrient().getProtein();
        response.fat = bookMarkedRaw.getNutrient().getFat();
        response.calcium = bookMarkedRaw.getNutrient().getCalcium();
        response.phosphorus = bookMarkedRaw.getNutrient().getPhosphorus();
        response.vitaminA = bookMarkedRaw.getNutrient().getVitamin().getVitaminA();
        response.vitaminD = bookMarkedRaw.getNutrient().getVitamin().getVitaminD();
        response.vitaminE = bookMarkedRaw.getNutrient().getVitamin().getVitaminE();

        return response;
    }
}
