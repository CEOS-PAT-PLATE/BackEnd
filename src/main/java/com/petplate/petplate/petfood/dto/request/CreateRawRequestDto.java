package com.petplate.petplate.petfood.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class CreateRawRequestDto {

    @NotNull(message = "기준 제공량(g)을 입력해주세요")
    @Min(0)
    private double standardAmount;

    @NotBlank(message = "식품 이름을 입력해주세요")
    private String name;

    private String description;

    @NotNull(message = "칼로리를 입력해주세요")
    @Min(0)
    private double kcal;

    @NotNull(message = "탄수화물(g)을 입력해주세요")
    @Min(0)
    private double carbonHydrate;

    @NotNull(message = "단백질(g)을 입력해주세요")
    @Min(0)
    private double protein;

    @NotNull(message = "지방(g)을 입력해주세요")
    @Min(0)
    private double fat;

    @NotNull(message = "칼슘(g)을 입력해주세요")
    @Min(0)
    private double calcium;

    @NotNull(message = "인(g)을 입력해주세요")
    @Min(0)
    private double phosphorus;

    @NotNull(message = "비타민 A(g)을 입력해주세요")
    @Min(0)
    private double vitaminA;

    @NotNull(message = "비타민 D(g)을 입력해주세요")
    @Min(0)
    private double vitaminD;

    @NotNull(message = "비타민 E(g)을 입력해주세요")
    @Min(0)
    private double vitaminE;
}
