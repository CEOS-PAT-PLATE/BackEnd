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
    private double vitaminA;  // 단위 mcg RAE

    @Column(nullable = false)
    private double vitaminB;  // 단위 mg

    @Column(nullable = false)
    private double vitaminD;  // 단위 mcg

    @Column(nullable = false)
    private double vitaminE;  // 단위 mcg RAE

    @Builder
    public Vitamin(double vitaminA, double vitaminB, double vitaminD, double vitaminE) {
        this.vitaminA = vitaminA;
        this.vitaminB = vitaminB;
        this.vitaminD = vitaminD;
        this.vitaminE = vitaminE;
    }
}
