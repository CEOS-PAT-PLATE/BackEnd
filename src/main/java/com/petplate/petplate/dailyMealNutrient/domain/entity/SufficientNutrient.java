package com.petplate.petplate.dailyMealNutrient.domain.entity;

import com.petplate.petplate.petdailymeal.domain.entity.DailyMeal;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(exclude = {"dailyMeal"})
public class SufficientNutrient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;  // 이름
    @Column(nullable = false)
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
    private DailyMeal dailyMeal;

    @Builder
    public SufficientNutrient(String name, String unit, String description, double amount, double properAmount, double maximumAmount, DailyMeal dailyMeal) {
        this.name = name;
        this.unit = unit;
        this.description = description;
        this.amount = amount;
        this.properAmount = properAmount;
        this.maximumAmount = maximumAmount;
        this.dailyMeal = dailyMeal;
    }
}