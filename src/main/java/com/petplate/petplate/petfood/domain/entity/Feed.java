package com.petplate.petplate.petfood.domain.entity;

import com.petplate.petplate.common.EmbeddedType.Nutrient;
import com.petplate.petplate.petdailymeal.domain.entity.DailyMeal;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Feed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double serving;

    @Column(nullable = false)
    private double kcal;

    @Embedded
    private Nutrient nutrient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_meal_id",nullable = false)
    private DailyMeal dailyMeal;


    @Builder
    public Feed(String name, double serving, double kcal, Nutrient nutrient,
                DailyMeal dailyMeal) {
        this.name = name;
        this.serving = serving;
        this.kcal = kcal;
        this.nutrient = nutrient;
        this.dailyMeal = dailyMeal;
    }
}
