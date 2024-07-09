package com.petplate.petplate.petfood.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateBookMarkedFeedRequestDto {
    @Min(0)
    @NotBlank(message = "사료 총량(g)을 입력해주세요")
    private double serving;
    @NotBlank(message = "사료 이름을 입력해주세요")
    private String name;

    @Min(0)
    @NotBlank(message = "칼로리를 입력해주세요")
    private double kcal;
    @Min(0)
    @NotBlank(message = "탄수화물(%)을 입력해주세요")
    private double carbonHydratePercent;
    @Min(0)
    @NotBlank(message = "단백질(%)을 입력해주세요")
    private double proteinPercent;
    @Min(0)
    @NotBlank(message = "지방(%)을 입력해주세요")
    private double fatPercent;
    @Min(0)
    @NotBlank(message = "칼슘(%)을 입력해주세요")
    private double calciumPercent;
    @Min(0)
    @NotBlank(message = "인(%)을 입력해주세요")
    private double phosphorusPercent;
    @Min(0)
    @NotBlank(message = "비타민 A(%)을 입력해주세요")
    private double vitaminAPercent;
    @Min(0)
    @NotBlank(message = "비타민 D(%)을 입력해주세요")
    private double vitaminDPercent;
    @Min(0)
    @NotBlank(message = "비타민 E(%)을 입력해주세요")
    private double vitaminEPercent;
}
