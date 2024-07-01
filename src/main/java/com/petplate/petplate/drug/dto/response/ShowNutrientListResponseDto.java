package com.petplate.petplate.drug.dto.response;

import com.petplate.petplate.common.EmbeddedType.StandardNutrient;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShowNutrientListResponseDto {

    private List<String> nutrientList;

    public static ShowNutrientListResponseDto from(final List<String> nutrientList){
        return new ShowNutrientListResponseDto(nutrientList);
    }

}
