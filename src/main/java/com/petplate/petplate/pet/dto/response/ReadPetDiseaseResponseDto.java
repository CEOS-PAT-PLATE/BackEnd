package com.petplate.petplate.pet.dto.response;

import com.petplate.petplate.medicalcondition.domain.entity.Disease;
import lombok.Getter;

@Getter
public class ReadPetDiseaseResponseDto {
    private String name;
    private String description;

    public static ReadPetDiseaseResponseDto from(Disease disease) {
        ReadPetDiseaseResponseDto response = new ReadPetDiseaseResponseDto();
        response.name = disease.getName();
        response.description = disease.getDescription();
        return response;
    }
}
