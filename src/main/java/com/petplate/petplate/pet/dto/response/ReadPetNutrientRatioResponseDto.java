package com.petplate.petplate.pet.dto.response;

import com.petplate.petplate.common.EmbeddedType.Nutrient;
import com.petplate.petplate.common.EmbeddedType.StandardNutrient;
import lombok.Getter;

import java.util.Map;

@Getter
public class ReadPetNutrientRatioResponseDto {
    // 탄수화물의 경우 적정 영양분 계산 식이 없어서 제외함 (추후 추가 가능)
    private double protein;  // 단위 g
    private double fat;  // 단위 g
    private double calcium;  // 단위 g
    private double phosphorus;  // 단위 g
    private double vitaminA;  // 단위 mcg RAE
    private double vitaminB;  // 단위 mg
    private double vitaminD;  // 단위 mcg
    private double vitaminE;  // 단위 mcg RAE

    public static ReadPetNutrientRatioResponseDto of(Nutrient nutrient, double weight) {
        // 반려견의 몸무게와 하루 먹은 식사의 영양분을 바탕으로 영양소별 적정량 대비 섭취 비율을 계산
        Map<StandardNutrient, Double> nutrientsMap =
                StandardNutrient.getNutrientsMap(nutrient, weight);

        // 영양분 별 비율
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
