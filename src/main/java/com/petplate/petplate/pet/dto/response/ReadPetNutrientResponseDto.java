package com.petplate.petplate.pet.dto.response;

import lombok.Getter;

@Getter
public class ReadPetNutrientResponseDto {
    private String name;
    private String unit;
    private String description;
    private double amount;

    public static ReadPetNutrientResponseDto from(String name, String unit, String description, double amount) {
        ReadPetNutrientResponseDto response = new ReadPetNutrientResponseDto();
        response.name = name;
        response.unit = unit;
        response.description = description;
        response.amount = amount;

        return response;
    }
}
