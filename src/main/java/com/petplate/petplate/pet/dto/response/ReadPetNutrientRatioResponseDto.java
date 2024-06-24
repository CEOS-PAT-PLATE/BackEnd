package com.petplate.petplate.pet.dto.response;

import com.petplate.petplate.common.EmbeddedType.Nutrient;
import com.petplate.petplate.common.EmbeddedType.StandardNutrient;
import lombok.Getter;

import java.util.Map;

@Getter
public class ReadPetNutrientRatioResponseDto {
    private double carbonHydrate;
    private double protein;
    private double fat;
    private double calcium;
    private double phosphorus;
    private double vitaminA;
    private double vitaminB;
    private double vitaminD;
    private double vitaminE;

    public static ReadPetNutrientRatioResponseDto of(Nutrient nutrient, double weight) {
        // 반려견의 몸무게와 하루 먹은 식사의 영양분을 바탕으로 영양소별 적정량 대비 섭취 비율을 계산
        Map<StandardNutrient, Double> nutrientsMap =
                StandardNutrient.getNutrientsMap(nutrient, weight);

        // 영양분 별 비율
        Double carbonHydrate = nutrientsMap.get(StandardNutrient.CARBON_HYDRATE);
        Double protein = nutrientsMap.get(StandardNutrient.PROTEIN);
        Double fat = nutrientsMap.get(StandardNutrient.FAT);
        Double calcium = nutrientsMap.get(StandardNutrient.CALCIUM);
        Double phosphorus = nutrientsMap.get(StandardNutrient.PHOSPHORUS);
        Double vitaminA = nutrientsMap.get(StandardNutrient.VITAMIN_A);
        Double vitaminB = nutrientsMap.get(StandardNutrient.VITAMIN_B);
        Double vitaminD = nutrientsMap.get(StandardNutrient.VITAMIN_D);
        Double vitaminE = nutrientsMap.get(StandardNutrient.VITAMIN_E);

        ReadPetNutrientRatioResponseDto response =
                new ReadPetNutrientRatioResponseDto();

        response.carbonHydrate = carbonHydrate;
        response.protein = protein;
        response.fat = fat;
        response.calcium = calcium;
        response.phosphorus = phosphorus;
        response.vitaminA = vitaminA;
        response.vitaminB = vitaminB;
        response.vitaminD = vitaminD;
        response.vitaminE = vitaminE;

        return response;
    }
}
