package com.petplate.petplate.petdailymeal.dto.response;

import com.petplate.petplate.petdailymeal.domain.entity.DailyRaw;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReadDailyRawWithRawIdResponseDto {
    private Long rawId;
    private Long dailyRawId;
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

    public static ReadDailyRawWithRawIdResponseDto from(DailyRaw dailyRaw) {
        ReadDailyRawWithRawIdResponseDto response
                = new ReadDailyRawWithRawIdResponseDto();

        response.rawId = dailyRaw.getRaw() != null ? dailyRaw.getRaw().getId() : null;
        response.dailyRawId = dailyRaw.getId();
        response.name = dailyRaw.getRaw().getName();
        response.description = dailyRaw.getRaw().getDescription();
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
}
