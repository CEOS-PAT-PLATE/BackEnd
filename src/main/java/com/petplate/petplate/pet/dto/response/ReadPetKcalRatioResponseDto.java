package com.petplate.petplate.pet.dto.response;

import lombok.Getter;

@Getter
public class ReadPetKcalRatioResponseDto {
    private double ratio;

    public static ReadPetKcalRatioResponseDto of(double ratio) {
        ReadPetKcalRatioResponseDto response = new ReadPetKcalRatioResponseDto();
        response.ratio = ratio;

        return response;
    }
}
