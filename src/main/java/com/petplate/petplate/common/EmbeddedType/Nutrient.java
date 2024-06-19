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
    private float carbonHydrate;

    @Column(nullable = false)
    private float protein;

    @Column(nullable = false)
    private float fat;

    @Column(nullable = false)
    private float calcium;

    @Column(nullable = false)
    private float phosphorus;

    @Embedded
    private Vitamin vitamin;

    @Builder
    public Nutrient(float carbonHydrate, float protein, float fat, float calcium, float phosphorus,
            Vitamin vitamin) {
        this.carbonHydrate = carbonHydrate;
        this.protein = protein;
        this.fat = fat;
        this.calcium = calcium;
        this.phosphorus = phosphorus;
        this.vitamin = vitamin;
    }
}
