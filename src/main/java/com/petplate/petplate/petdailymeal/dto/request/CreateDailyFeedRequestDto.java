package com.petplate.petplate.petdailymeal.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateDailyFeedRequestDto {
    @Min(1)
    @NotNull(message = "사료 총량(g)을 입력해주세요")
    private double serving;
    @NotBlank(message = "사료 이름을 입력해주세요")
    private String name;

    @Min(0)
    @NotNull(message = "칼로리를 입력해주세요")
    private double kcal;
    @Min(0)
    @Max(100)
    @NotNull(message = "탄수화물(%)을 입력해주세요")
    private double carbonHydratePercent;
    @Min(0)
    @Max(100)
    @NotNull(message = "단백질(%)을 입력해주세요")
    private double proteinPercent;
    @Min(0)
    @Max(100)
    @NotNull(message = "지방(%)을 입력해주세요")
    private double fatPercent;
    @Min(0)
    @Max(100)
    @NotNull(message = "칼슘(%)을 입력해주세요")
    private double calciumPercent;
    @Min(0)
    @Max(100)
    @NotNull(message = "인(%)을 입력해주세요")
    private double phosphorusPercent;
    @Min(0)
    @Max(100)
    @NotNull(message = "비타민 A(%)을 입력해주세요")
    private double vitaminAPercent;
    @Min(0)
    @Max(100)
    @NotNull(message = "비타민 D(%)을 입력해주세요")
    private double vitaminDPercent;
    @Min(0)
    @Max(100)
    @NotNull(message = "비타민 E(%)을 입력해주세요")
    private double vitaminEPercent;
}
