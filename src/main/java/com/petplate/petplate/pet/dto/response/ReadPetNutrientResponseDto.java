package com.petplate.petplate.pet.dto.response;

import com.petplate.petplate.common.EmbeddedType.Nutrient;
import com.petplate.petplate.petdailymeal.domain.entity.DailyMeal;
import lombok.Getter;

@Getter
public class ReadPetNutrientResponseDto {
    private String name;
    private String unit;
    private String description;
    private double weight;

    public static ReadPetNutrientResponseDto from(String name, String unit, String description,double weight) {
        ReadPetNutrientResponseDto response = new ReadPetNutrientResponseDto();
        response.name = name;
        response.unit = unit;
        response.description = description;
        response.weight = weight;

        return response;
    }
}
