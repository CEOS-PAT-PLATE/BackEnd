package com.petplate.petplate.common.EmbeddedType;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.*;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
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

    public double getNutrientAmountByName(String name) {
        if(name.equals(StandardNutrient.CARBON_HYDRATE.getName())) return carbonHydrate;
        else if(name.equals(StandardNutrient.PROTEIN.getName())) return protein;
        else if(name.equals(StandardNutrient.FAT.getName())) return fat;
        else if(name.equals(StandardNutrient.CALCIUM.getName())) return calcium;
        else if(name.equals(StandardNutrient.PHOSPHORUS.getName())) return phosphorus;
        else if(name.equals(StandardNutrient.VITAMIN_A.getName())) return vitamin.getVitaminA();
        else if(name.equals(StandardNutrient.VITAMIN_D.getName())) return vitamin.getVitaminD();
        else if(name.equals(StandardNutrient.VITAMIN_E.getName())) return vitamin.getVitaminE();
        else throw new IllegalArgumentException("Invalid Nutrient name: " + name);
    }
}
