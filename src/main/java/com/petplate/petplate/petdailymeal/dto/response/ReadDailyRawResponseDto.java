package com.petplate.petplate.petdailymeal.dto.response;

import com.petplate.petplate.petdailymeal.domain.entity.DailyRaw;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class ReadDailyRawResponseDto {
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

    public static ReadDailyRawResponseDto from(DailyRaw dailyRaw) {
        ReadDailyRawResponseDto response
                = new ReadDailyRawResponseDto();

        response.dailyRawId = dailyRaw.getId();
        response.name = dailyRaw.getRaw()!=null?dailyRaw.getRaw().getName():"존재하지 않는 음식입니다";
        response.description = dailyRaw.getRaw() != null ? dailyRaw.getRaw().getDescription() : null;
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
