package com.petplate.petplate.drug.dto.request;

import com.petplate.petplate.common.EmbeddedType.StandardNutrient;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DrugSaveRequestDto {

    @NotBlank(message = "약 한글이름을 입력해주세요")
    private String name;

    @NotBlank(message = "약 영문이름을 입력해주세요")
    private String englishName;

    @NotBlank(message = "제조사를 입력해주세요")
    private String vendor;

    @NotBlank(message = "이미지 경로를 입력해주세요")
    private String drugImgPath;

    @NotBlank(message = "구입 경로를 입력해주세요")
    private String url;

    private Set<String> efficientNutrients = new HashSet<>();


}
