package com.petplate.petplate.petfood.dto.response;

import com.petplate.petplate.petfood.domain.entity.Raw;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReadRawResponseDto {
    private Long id;
    private double standardAmount;
    private String name;
    private String description;
    private double kcal;
    private double carbonHydrate;
    private double protein;
    private double fat;
    private double calcium;
    private double phosphorus;
    private double vitaminA;
    private double vitaminD;
    private double vitaminE;

    public static ReadRawResponseDto from(Raw raw) {
        ReadRawResponseDto response
                = new ReadRawResponseDto();
        response.id = raw.getId();
        response.standardAmount = raw.getStandardAmount();
        response.name = raw.getName();
        response.description = raw.getDescription();
        response.kcal = raw.getKcal();
        response.carbonHydrate = raw.getNutrient().getCarbonHydrate();
        response.protein = raw.getNutrient().getProtein();
        response.fat = raw.getNutrient().getFat();
        response.calcium = raw.getNutrient().getCalcium();
        response.phosphorus = raw.getNutrient().getPhosphorus();
        response.vitaminA = raw.getNutrient().getVitamin().getVitaminA();
        response.vitaminD = raw.getNutrient().getVitamin().getVitaminD();
        response.vitaminE = raw.getNutrient().getVitamin().getVitaminE();

        return response;
    }
}
