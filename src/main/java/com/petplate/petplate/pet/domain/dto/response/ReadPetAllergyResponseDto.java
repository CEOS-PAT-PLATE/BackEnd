package com.petplate.petplate.pet.domain.dto.response;

import com.petplate.petplate.medicalcondition.domain.entity.Allergy;
import jakarta.persistence.Column;
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
