package com.petplate.petplate.petfood.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateRawRequestDto {

    @Min(0)
    @NotBlank(message = "기준 제공량(g)을 입력해주세요")
    private double standardAmount;
    @NotBlank(message = "식품 이름을 입력해주세요")
    private String name;
    @Min(0)
    @NotBlank(message = "칼로리를 입력해주세요")
    private double kcal;
    @Min(0)
    @NotBlank(message = "탄수화물(g)을 입력해주세요")
    private double carbonHydrate;
    @Min(0)
    @NotBlank(message = "단백질(g)을 입력해주세요")
    private double protein;
    @Min(0)
    @NotBlank(message = "지방(g)을 입력해주세요")
    private double fat;
    @Min(0)
    @NotBlank(message = "칼슘(g)을 입력해주세요")
    private double calcium;
    @Min(0)
    @NotBlank(message = "인(g)을 입력해주세요")
    private double phosphorus;
    @Min(0)
    @NotBlank(message = "비타민 A(g)을 입력해주세요")
    private double vitaminA;
    @Min(0)
    @NotBlank(message = "비타민 D(g)을 입력해주세요")
    private double vitaminD;
    @Min(0)
    @NotBlank(message = "비타민 E(g)을 입력해주세요")
    private double vitaminE;
}
