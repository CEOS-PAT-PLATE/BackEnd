package com.petplate.petplate.pet.dto.response;

import lombok.Getter;

@Getter
public class ReadPetKcalResponseDto {
    private double kcal;

    public static ReadPetKcalResponseDto of(double kcal) {
        ReadPetKcalResponseDto response = new ReadPetKcalResponseDto();
        response.kcal = kcal;

        return response;
    }
}
