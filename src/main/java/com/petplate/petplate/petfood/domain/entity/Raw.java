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
    private int totalAmount;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private float kcal;

    @Embedded
    private Nutrient nutrient;

    @Builder
    public Raw(int totalAmount, String name, float kcal, Nutrient nutrient) {
        this.totalAmount = totalAmount;
        this.name = name;
        this.kcal = kcal;
        this.nutrient = nutrient;
    }
}
