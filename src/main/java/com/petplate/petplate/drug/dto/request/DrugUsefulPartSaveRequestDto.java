package com.petplate.petplate.drug.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DrugUsefulPartSaveRequestDto {

    @Schema(description = "영양제에 좋은 부분",example = "털")
    private String name;

}
