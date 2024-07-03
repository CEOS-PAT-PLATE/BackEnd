package com.petplate.petplate.petdailymeal.dto.response;

import com.petplate.petplate.petdailymeal.domain.entity.DailyBookMarkedRaw;
import com.petplate.petplate.petdailymeal.domain.entity.DailyRaw;
import com.petplate.petplate.petfood.domain.entity.BookMarkedRaw;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReadDailyRawBookMarkedRawResponseDto {
    private Long id;
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

    public static ReadDailyRawBookMarkedRawResponseDto from(DailyRaw dailyRaw) {
        ReadDailyRawBookMarkedRawResponseDto response
                = new ReadDailyRawBookMarkedRawResponseDto();

        response.id = dailyRaw.getId();
        response.name = dailyRaw.getRaw().getName();
//        response.description = dailyRaw.getRaw().getDescription() != null ? dailyRaw.getRaw().getDescription() : null;
        response.serving = dailyRaw.getServing();
        response.kcal = dailyRaw.getKcal();
        response.carbonHydrate = dailyRaw.getNutrient().getCarbonHydrate();
        response.protein = dailyRaw.getNutrient().getProtein();
        response.fat = dailyRaw.getNutrient().getFat();
        response.calcium = dailyRaw.getNutrient().getCalcium();
        response.phosphorus = dailyRaw.getNutrient().getPhosphorus();
        response.vitaminA = dailyRaw.getNutrient().getVitamin().getVitaminA();
        response.vitaminD = dailyRaw.getNutrient().getVitamin().getVitaminD();
        response.vitaminE = dailyRaw.getNutrient().getVitamin().getVitaminE();

        return response;
    }

    public static ReadDailyRawBookMarkedRawResponseDto from(BookMarkedRaw bookMarkedRaw) {
        ReadDailyRawBookMarkedRawResponseDto response
                = new ReadDailyRawBookMarkedRawResponseDto();

        response.id = bookMarkedRaw.getId();
        response.name = bookMarkedRaw.getRaw().getName();
//        response.description = bookMarkedRaw.getRaw().getDescription() != null ? bookMarkedRaw.getRaw().getDescription() : null;
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
