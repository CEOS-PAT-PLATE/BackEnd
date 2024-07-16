package com.petplate.petplate.petdailymeal.dto.response;

import com.petplate.petplate.petdailymeal.domain.entity.DailyBookMarkedFeed;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class ReadDailyBookMarkedFeedResponseDto {
    private Long dailyBookMarkedFeedId;
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

    public static ReadDailyBookMarkedFeedResponseDto from(DailyBookMarkedFeed dailybookMarkedFeed) {
        ReadDailyBookMarkedFeedResponseDto response
                = new ReadDailyBookMarkedFeedResponseDto();

        response.dailyBookMarkedFeedId = dailybookMarkedFeed.getId();
        if (dailybookMarkedFeed.getBookMarkedFeed() != null) {
            response.name = dailybookMarkedFeed.getBookMarkedFeed().getName();
            response.serving = dailybookMarkedFeed.getBookMarkedFeed().getServing();
            response.kcal = dailybookMarkedFeed.getBookMarkedFeed().getKcal();
            response.carbonHydrate = dailybookMarkedFeed.getBookMarkedFeed().getNutrient().getCarbonHydrate();
            response.protein = dailybookMarkedFeed.getBookMarkedFeed().getNutrient().getProtein();
            response.fat = dailybookMarkedFeed.getBookMarkedFeed().getNutrient().getFat();
            response.calcium = dailybookMarkedFeed.getBookMarkedFeed().getNutrient().getCalcium();
            response.phosphorus = dailybookMarkedFeed.getBookMarkedFeed().getNutrient().getPhosphorus();
            response.vitaminA = dailybookMarkedFeed.getBookMarkedFeed().getNutrient().getVitamin().getVitaminA();
            response.vitaminD = dailybookMarkedFeed.getBookMarkedFeed().getNutrient().getVitamin().getVitaminD();
            response.vitaminE = dailybookMarkedFeed.getBookMarkedFeed().getNutrient().getVitamin().getVitaminE();
        } else{
            response.name = "존재하지 않는 음식입니다";
        }

        return response;
    }
}
