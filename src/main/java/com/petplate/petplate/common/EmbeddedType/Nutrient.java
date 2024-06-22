package com.petplate.petplate.common.EmbeddedType;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
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
}
