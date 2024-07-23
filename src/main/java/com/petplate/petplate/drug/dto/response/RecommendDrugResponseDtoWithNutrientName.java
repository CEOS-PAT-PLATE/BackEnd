package com.petplate.petplate.drug.dto.response;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecommendDrugResponseDtoWithNutrientName {

    private String nutrientName;
    private List<RecommendDrugResponseDto> drugResponseDtoList = new ArrayList<>();


    public static RecommendDrugResponseDtoWithNutrientName of(final String nutrientName,final List<RecommendDrugResponseDto> recommendDrugResponseDtoList){
        return new RecommendDrugResponseDtoWithNutrientName(nutrientName,recommendDrugResponseDtoList);
    }

}
