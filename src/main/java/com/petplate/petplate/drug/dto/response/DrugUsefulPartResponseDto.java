package com.petplate.petplate.drug.dto.response;

import com.petplate.petplate.drug.domain.entity.DrugUsefulPart;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DrugUsefulPartResponseDto {

    private Long drugUsefulPartId;
    private String name;

    public static DrugUsefulPartResponseDto of(final DrugUsefulPart drugUsefulPart){

        return new DrugUsefulPartResponseDto(drugUsefulPart.getId(),drugUsefulPart.getName());

    }

}
