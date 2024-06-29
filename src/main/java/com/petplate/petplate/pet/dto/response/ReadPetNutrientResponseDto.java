package com.petplate.petplate.pet.dto.response;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ReadPetNutrientResponseDto {
    private String name;  // 이름
    private String unit;  // 영양소 단위
    private String description;  // 영양소 설명
    private double amount;  // 섭취량
    private double minimumIntake; // 최소 섭취량
    private double maximumIntake; // 최대 섭취량
    private double amountRatioPerMinimumIntake;  // 최소 섭취량 대비 섭취량 비율
    private double maximumIntakeRatioPerMinimumIntake;  // 최소 섭취량 대비 최대 섭취량 비율

    public static ReadPetNutrientResponseDto of(String name, String unit, String description, double amount, double minimumIntake, double maximumIntake) {
        ReadPetNutrientResponseDto response = new ReadPetNutrientResponseDto();
        response.name = name;
        response.unit = unit;
        response.description = description;
        response.amount = amount;
        response.minimumIntake = minimumIntake;
        response.maximumIntake = maximumIntake;
        response.amountRatioPerMinimumIntake = amount / minimumIntake;
        response.maximumIntakeRatioPerMinimumIntake = maximumIntake / minimumIntake;

        return response;
    }
}
