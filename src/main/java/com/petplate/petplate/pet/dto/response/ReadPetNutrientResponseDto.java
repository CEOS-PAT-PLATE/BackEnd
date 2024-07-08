package com.petplate.petplate.pet.dto.response;

import lombok.Getter;
import lombok.ToString;

@Getter
public class ReadPetNutrientResponseDto {
    private String name;  // 이름
    private String unit;  // 영양소 단위
    private String description;  // 영양소 설명
    private double amount;  // 섭취량
    private double properAmount; // 적정 섭취량
    private double maximumAmount; // 최대 섭취량
    private double maximumAmountRatioPerProperAmount;  // 적정 섭취량 대비 최대 섭취 허용량 비율
    private double amountRatioPerProperAmount;  // 최소 섭취량 대비 섭취량 비율
    private double AmountRatioPerMaximumAmount;  // 최대 섭취량 대비 섭취량 비율

    public static ReadPetNutrientResponseDto of(String name, String unit, String description, double amount, double properAmount, double maximumAmount) {
        ReadPetNutrientResponseDto response = new ReadPetNutrientResponseDto();
        response.name = name;
        response.unit = unit;
        response.description = description;
        response.amount = amount;
        response.properAmount = properAmount;
        response.maximumAmount = maximumAmount;
        response.maximumAmountRatioPerProperAmount = maximumAmount / properAmount;
        response.amountRatioPerProperAmount = amount / properAmount;
        response.AmountRatioPerMaximumAmount = amount / maximumAmount;

        return response;
    }

    @Override
    public String toString() {
        return "ReadPetNutrientResponseDto{" +
                "이름='" + name + '\'' +
                ", 단위='" + unit + '\'' +
                ", 영양소 설명='" + description + '\'' +
                ", 섭취량=" + amount +
                ", 적정 섭취량=" + properAmount +
                ", 최대 허용 섭취량=" + maximumAmount +
                ", 최대 허용 섭취량 / 적정 섭취량 =" + maximumAmountRatioPerProperAmount +
                ", 섭취량 / 적정 섭취량=" + amountRatioPerProperAmount +
                ", 섭취량 / 최대 허용 섭취량=" + AmountRatioPerMaximumAmount +
                "}\n";
    }
}
