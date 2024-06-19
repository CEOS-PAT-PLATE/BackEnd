package com.petplate.petplate.petdailymeal.domain.entity;

import com.petplate.petplate.medicalcondition.domain.entity.Disease;
import com.petplate.petplate.petfood.domain.entity.Raw;
import jakarta.persistence.Column;
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
public class RawDailyMeal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "raw_daily_meal_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_meal_id",nullable = false)
    private DailyMeal dailyMeal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "raw_id",nullable = false)
    private Raw raw;

    @Column(nullable = false)
    private float weight;

    @Builder
    public RawDailyMeal(DailyMeal dailyMeal, Raw raw, float weight) {
        this.dailyMeal = dailyMeal;
        this.raw = raw;
        this.weight = weight;
    }
}
