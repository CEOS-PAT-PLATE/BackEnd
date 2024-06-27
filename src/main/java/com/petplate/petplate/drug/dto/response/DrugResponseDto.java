package com.petplate.petplate.drug.dto.response;

import com.petplate.petplate.common.EmbeddedType.StandardNutrient;
import com.petplate.petplate.drug.domain.entity.Drug;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DrugResponseDto {

    private String name;
    private String englishName;
    private String vendor;
    private String drugImgPath;
    private String url;
    private List<String> nutrientsName = new ArrayList<>();


    public static DrugResponseDto of(final Drug drug){
        return new DrugResponseDto(drug.getName(),drug.getEnglishName(), drug.getVendor(),
                drug.getDrugImgPath(), drug.getUrl(), drug.getEfficientNutrient().stream().map(
                StandardNutrient::getName).collect(Collectors.toList()));
    }

}
