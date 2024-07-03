package com.petplate.petplate.petfood.domain.entity;

import com.petplate.petplate.common.EmbeddedType.Nutrient;
import com.petplate.petplate.common.Inheritance.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Raw extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "raw_id")
    private Long id;

    @Column(nullable = false)
    private double standardAmount;

    @Column(nullable = true)
    private String description;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private double kcal;

    @Embedded
    private Nutrient nutrient;

    @Builder
    public Raw(double standardAmount, String name, String description, double kcal, Nutrient nutrient) {
        this.standardAmount = standardAmount;
        this.name = name;
        this.description = description;
        this.kcal = kcal;
        this.nutrient = nutrient;
    }
}
