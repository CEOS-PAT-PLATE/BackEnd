package com.petplate.petplate.common.EmbeddedType;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Nutrient {

    @Column(nullable = false)
    private double carbonHydrate;  // 단위 g

    @Column(nullable = false)
    private double protein;  // 단위 g

    @Column(nullable = false)
    private double fat;  // 단위 g

    @Column(nullable = false)
    private double calcium;  // 단위 g

    @Column(nullable = false)
    private double phosphorus;  // 단위 g

    @Embedded
    private Vitamin vitamin;

    @Builder
    public Nutrient(double carbonHydrate, double protein, double fat, double calcium, double phosphorus,
                    Vitamin vitamin) {
        this.carbonHydrate = carbonHydrate;
        this.protein = protein;
        this.fat = fat;
        this.calcium = calcium;
        this.phosphorus = phosphorus;
        this.vitamin = vitamin;
    }

    public double getNutrientWeightByName(String name) {
        return switch (name) {
            case "carbonHydrate" -> carbonHydrate;
            case "탄수화물" -> carbonHydrate;
            case "protein" -> protein;
            case "단백질" -> protein;
            case "fat" -> fat;
            case "지방" -> fat;
            case "calcium" -> calcium;
            case "칼슘" -> calcium;
            case "phosphorus" -> phosphorus;
            case "인" -> phosphorus;
            case "vitaminA" -> vitamin.getVitaminA();
            case "비타민A" -> vitamin.getVitaminA();
            case "vitaminB" -> vitamin.getVitaminB();
            case "비타민B" -> vitamin.getVitaminB();
            case "vitaminD" -> vitamin.getVitaminD();
            case "비타민D" -> vitamin.getVitaminD();
            case "vitaminE" -> vitamin.getVitaminE();
            case "비타민E" -> vitamin.getVitaminE();
            default -> throw new RuntimeException("Unknown nutrient name: " + name);
        };
    }
}
