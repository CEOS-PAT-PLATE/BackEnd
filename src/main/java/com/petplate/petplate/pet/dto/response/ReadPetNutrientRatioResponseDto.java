package com.petplate.petplate.pet.dto.response;

import com.petplate.petplate.common.EmbeddedType.Nutrient;
import com.petplate.petplate.common.EmbeddedType.StandardNutrient;
import com.petplate.petplate.pet.domain.Activity;
import lombok.Getter;

import java.util.Map;

@Getter
public class ReadPetNutrientRatioResponseDto {
    private String name;  // 영양소 명
    private double ratio; // 적정 섭취량 대비 섭취량

    public static ReadPetNutrientRatioResponseDto of(String name, double ratio) {

        ReadPetNutrientRatioResponseDto response =
                new ReadPetNutrientRatioResponseDto();

        response.name = name;
        response.ratio = ratio;

        return response;
    }
}
