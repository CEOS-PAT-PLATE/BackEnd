package com.petplate.petplate.petdailymeal.domain.entity;

import com.petplate.petplate.common.EmbeddedType.Nutrient;
import com.petplate.petplate.common.Inheritance.BaseEntity;
import com.petplate.petplate.pet.domain.entity.Pet;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DailyMeal extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "daily_meal_id")
    private Long id;

    @Column(nullable = false)
    private float kcal;

    @Embedded
    private Nutrient nutrient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id",nullable = false)
    private Pet pet;

    @Builder
    public DailyMeal(float kcal, Nutrient nutrient, Pet pet) {
        this.kcal = kcal;
        this.nutrient = nutrient;
        this.pet = pet;
    }
}
