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
    private double carbonHydrate;

    @Column(nullable = false)
    private double protein;

    @Column(nullable = false)
    private double fat;

    @Column(nullable = false)
    private double calcium;

    @Column(nullable = false)
    private double phosphorus;

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
