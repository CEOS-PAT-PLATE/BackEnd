package com.petplate.petplate.dailyMealNutrient.domain.entity;

import com.petplate.petplate.common.Inheritance.BaseEntity;
import com.petplate.petplate.petdailymeal.domain.entity.DailyMeal;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(exclude = {"dailyMeal"})
public class ProperNutrient extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "proper_nutrient_id")
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;  // 이름
    @Column(length = 10, nullable = false)
    private String unit;  // 영양소 단위
    @Column(nullable = false)
    private String description;  // 영양소 설명
    @Column(nullable = false)
    private double amount;  // 섭취량
    @Column(nullable = false)
    private double properAmount; // 적정 섭취량
    @Column(nullable = false)
    private double maximumAmount; // 최대 섭취량

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_meal_id", nullable = false)
    private DailyMeal dailyMeal;

    @Builder
    public ProperNutrient(String name, String unit, String description, double amount, double properAmount, double maximumAmount, DailyMeal dailyMeal) {
        this.name = name;
        this.unit = unit;
        this.description = description;
        this.amount = amount;
        this.properAmount = properAmount;
        this.maximumAmount = maximumAmount;
        this.dailyMeal = dailyMeal;
    }
}