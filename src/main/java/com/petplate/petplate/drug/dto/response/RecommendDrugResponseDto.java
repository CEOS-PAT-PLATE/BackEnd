package com.petplate.petplate.drug.dto.response;

import com.petplate.petplate.drug.domain.entity.Drug;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecommendDrugResponseDto {

    private Long id;
    private String name;
    private String englishName;
    private String vendor;
    private String drugImgPath;


    public static RecommendDrugResponseDto from(final Drug drug){
        return new RecommendDrugResponseDto(drug.getId(),drug.getName(),drug.getEnglishName(), drug.getVendor(),
                drug.getDrugImgPath());
    }



}
