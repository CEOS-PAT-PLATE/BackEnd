package com.petplate.petplate.common.EmbeddedType;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Vitamin {

    @Column(nullable = false)
    private double vitaminA;

    @Column(nullable = false)
    private double vitaminD;

    @Column(nullable = false)
    private double vitaminE;

    @Builder
    public Vitamin(double vitaminA, double vitaminD, double vitaminE) {
        this.vitaminA = vitaminA;
        this.vitaminD = vitaminD;
        this.vitaminE = vitaminE;
    }
}
