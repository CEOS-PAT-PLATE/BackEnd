package com.petplate.petplate.petfood.dto.request;

import com.petplate.petplate.common.EmbeddedType.Nutrient;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AddRawRequestDto {

    @NotBlank(message = "기준 제공량(g)을 입력해주세요")
    private double standardAmount;
    @NotBlank(message = "식품 이름을 입력해주세요")
    private String name;
    @NotBlank(message = "칼로리를 입력해주세요")
    private double kcal;
    @NotBlank(message = "탄수화물(g)을 입력해주세요")
    private double carbonHydrate;
    @NotBlank(message = "단백질(g)을 입력해주세요")
    private double protein;
    @NotBlank(message = "지방(g)을 입력해주세요")
    private double fat;
    @NotBlank(message = "칼슘(g)을 입력해주세요")
    private double calcium;
    @NotBlank(message = "인(g)을 입력해주세요")
    private double phosphorus;
    @NotBlank(message = "비타민 A(g)을 입력해주세요")
    private double vitaminA;
    @NotBlank(message = "비타민 D(g)을 입력해주세요")
    private double vitaminD;
    @NotBlank(message = "비타민 E(g)을 입력해주세요")
    private double vitaminE;
}
