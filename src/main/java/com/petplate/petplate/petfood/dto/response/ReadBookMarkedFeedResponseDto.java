package com.petplate.petplate.petfood.dto.response;

import com.petplate.petplate.petfood.domain.entity.BookMarkedFeed;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class ReadBookMarkedFeedResponseDto {
    private Long bookMarkedFeedId;
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

    public static ReadBookMarkedFeedResponseDto from(BookMarkedFeed bookMarkedFeed) {
        ReadBookMarkedFeedResponseDto response
                = new ReadBookMarkedFeedResponseDto();

        response.bookMarkedFeedId = bookMarkedFeed.getId();
        response.name = bookMarkedFeed.getName();
        response.serving = bookMarkedFeed.getServing();
        response.kcal = bookMarkedFeed.getKcal();
        response.carbonHydrate = bookMarkedFeed.getNutrient().getCarbonHydrate();
        response.protein = bookMarkedFeed.getNutrient().getProtein();
        response.fat = bookMarkedFeed.getNutrient().getFat();
        response.calcium = bookMarkedFeed.getNutrient().getCalcium();
        response.phosphorus = bookMarkedFeed.getNutrient().getPhosphorus();
        response.vitaminA = bookMarkedFeed.getNutrient().getVitamin().getVitaminA();
        response.vitaminD = bookMarkedFeed.getNutrient().getVitamin().getVitaminD();
        response.vitaminE = bookMarkedFeed.getNutrient().getVitamin().getVitaminE();

        return response;
    }
}