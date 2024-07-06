package com.petplate.petplate.petfood.dto.response;

import com.petplate.petplate.petfood.domain.entity.Feed;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class ReadFeedResponseDto {
    private Long feedId;
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

    public static ReadFeedResponseDto from(Feed feed) {
        ReadFeedResponseDto response
                = new ReadFeedResponseDto();

        response.feedId = feed.getId();
        response.name = feed.getName();
        response.serving = feed.getServing();
        response.kcal = feed.getKcal();
        response.carbonHydrate = feed.getNutrient().getCarbonHydrate();
        response.protein = feed.getNutrient().getProtein();
        response.fat = feed.getNutrient().getFat();
        response.calcium = feed.getNutrient().getCalcium();
        response.phosphorus = feed.getNutrient().getPhosphorus();
        response.vitaminA = feed.getNutrient().getVitamin().getVitaminA();
        response.vitaminD = feed.getNutrient().getVitamin().getVitaminD();
        response.vitaminE = feed.getNutrient().getVitamin().getVitaminE();

        return response;
    }
}
