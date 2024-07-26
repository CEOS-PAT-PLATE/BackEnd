package com.petplate.petplate.petdailymeal.dto.response;

import com.petplate.petplate.petdailymeal.domain.entity.DailyBookMarkedRaw;
import com.petplate.petplate.petfood.domain.entity.BookMarkedRaw;
import com.petplate.petplate.petfood.dto.response.ReadBookMarkedRawResponseDto;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class ReadDailyBookMarkedRawResponseDto {
    private Long dailyBookMarkedRawId;
    private String name;
    private String description;
    private Double serving;
    private Double kcal;
    private Double carbonHydrate;
    private Double protein;
    private Double fat;
    private Double calcium;
    private Double phosphorus;
    private Double vitaminA;
    private Double vitaminD;
    private Double vitaminE;

    public static ReadDailyBookMarkedRawResponseDto from(DailyBookMarkedRaw dailybookMarkedRaw) {
        ReadDailyBookMarkedRawResponseDto response
                = new ReadDailyBookMarkedRawResponseDto();

        response.dailyBookMarkedRawId = dailybookMarkedRaw.getId();
        if (dailybookMarkedRaw.getBookMarkedRaw() != null) {
            response.name = dailybookMarkedRaw.getBookMarkedRaw().getRaw() != null ? dailybookMarkedRaw.getBookMarkedRaw().getRaw().getName() : "존재하지 않는 음식입니다";
            response.description = dailybookMarkedRaw.getBookMarkedRaw().getRaw() != null ? dailybookMarkedRaw.getBookMarkedRaw().getDescription() : null;
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
        } else {
            response.name = "존재하지 않는 음식입니다";
        }
        return response;
    }
}
