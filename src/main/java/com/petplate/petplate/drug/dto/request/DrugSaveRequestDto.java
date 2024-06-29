package com.petplate.petplate.drug.dto.request;

import com.petplate.petplate.common.EmbeddedType.StandardNutrient;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "약 한글 이름",example = "맛있는 약")
    private String name;

    @NotBlank(message = "약 영문이름을 입력해주세요")
    @Schema(description = "약 영문 이름",example = "delicious drug")
    private String englishName;

    @NotBlank(message = "제조사를 입력해주세요")
    @Schema(description = "제조사 이름",example = "광동 제약")
    private String vendor;

    @NotBlank(message = "이미지 경로를 입력해주세요")
    @Schema(description = "이미지 경로",example = "www.img.path")
    private String drugImgPath;

    @NotBlank(message = "구입 경로를 입력해주세요")
    @Schema(description = "구입 경로 url",example = "www.naver.com")
    private String url;


    @Schema(description = "효과적인 영양소를 입력해주세요")
    private List<String> efficientNutrients = new ArrayList<>();


}
