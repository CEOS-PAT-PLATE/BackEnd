package com.petplate.petplate.petdailymeal.domain.entity;

import com.petplate.petplate.common.EmbeddedType.Nutrient;
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
public class DailyPackagedSnack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "daily_feed_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int totalAmount;

    @Column(nullable = false)
    private double kcal;

    @Embedded
    private Nutrient nutrient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_meal_id",nullable = false)
    private DailyMeal dailyMeal;

    @Builder
    public DailyPackagedSnack(String name, int totalAmount, double kcal, Nutrient nutrient,
            DailyMeal dailyMeal) {
        this.name = name;
        this.totalAmount = totalAmount;
        this.kcal = kcal;
        this.nutrient = nutrient;
        this.dailyMeal = dailyMeal;
    }
}
