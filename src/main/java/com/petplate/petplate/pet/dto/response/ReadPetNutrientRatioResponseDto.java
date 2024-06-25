package com.petplate.petplate.pet.dto.response;

import lombok.Getter;


@Getter
public class ReadPetNutrientRatioResponseDto {
    private String name;  // 영양소 명
    private double ratio; // 적정 섭취량 대비 섭취량 비율

    public static ReadPetNutrientRatioResponseDto of(String name, double ratio) {

        ReadPetNutrientRatioResponseDto response =
                new ReadPetNutrientRatioResponseDto();

        response.name = name;
        response.ratio = ratio;

        return response;
    }
}
