package com.petplate.petplate.petfood.domain.entity;

import com.petplate.petplate.common.EmbeddedType.Nutrient;
import com.petplate.petplate.common.Inheritance.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Raw extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "raw_id")
    private Long id;

    @Column(nullable = false)
    private double standardAmount;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private double kcal;

    @Embedded
    private Nutrient nutrient;

    @Builder
    public Raw(double standardAmount, String name, double kcal, Nutrient nutrient) {
        this.standardAmount = this.standardAmount;
        this.name = name;
        this.kcal = kcal;
        this.nutrient = nutrient;
    }
}
