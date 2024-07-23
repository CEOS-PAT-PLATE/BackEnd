package com.petplate.petplate.pet.dto.response;

import com.petplate.petplate.medicalcondition.domain.entity.Allergy;
import lombok.Getter;

@Getter
public class ReadPetAllergyResponseDto {
    private String name;
    private String description;

    public static ReadPetAllergyResponseDto from(Allergy allergy) {
        ReadPetAllergyResponseDto response = new ReadPetAllergyResponseDto();
        response.name = allergy.getName();
        response.description = allergy.getDescription();
        return response;
    }
}
