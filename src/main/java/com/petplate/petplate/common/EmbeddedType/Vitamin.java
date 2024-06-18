package com.petplate.petplate.common.EmbeddedType;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Vitamin {

    @Column(nullable = false)
    private float vitaminA;

    @Column(nullable = false)
    private float vitaminB;

    @Column(nullable = false)
    private float vitaminD;

    @Column(nullable = false)
    private float vitaminE;

    @Builder
    public Vitamin(float vitaminA, float vitaminB, float vitaminD, float vitaminE) {
        this.vitaminA = vitaminA;
        this.vitaminB = vitaminB;
        this.vitaminD = vitaminD;
        this.vitaminE = vitaminE;
    }
}
