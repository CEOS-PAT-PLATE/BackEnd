package com.petplate.petplate.petdailymeal.domain.entity;

import com.petplate.petplate.common.EmbeddedType.Nutrient;
import com.petplate.petplate.common.EmbeddedType.Vitamin;
import com.petplate.petplate.petfood.domain.entity.Raw;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class DailyRaw {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "daily_raw_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_meal_id", nullable = false)
    private DailyMeal dailyMeal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "raw_id")
    private Raw raw;

    @Column(nullable = false)
    private double serving;

    @Column(nullable = false)
    private double kcal;

    @Embedded
    private Nutrient nutrient;

    @Builder
    public DailyRaw(DailyMeal dailyMeal, Raw raw, double serving) {
        this.dailyMeal = dailyMeal;
        this.raw = raw;
        this.serving = serving;

        double ratio = (serving / raw.getStandardAmount());

        this.kcal = raw.getKcal() * ratio;

        Nutrient rawNutrient = raw.getNutrient();
        Vitamin vitamin = new Vitamin(rawNutrient.getVitamin().getVitaminA() * ratio,
                rawNutrient.getVitamin().getVitaminD() * ratio,
                rawNutrient.getVitamin().getVitaminE() * ratio);
        this.nutrient = new Nutrient(rawNutrient.getCarbonHydrate() * ratio,
                rawNutrient.getProtein() * ratio,
                rawNutrient.getFat() * ratio,
                rawNutrient.getCalcium() * ratio,
                rawNutrient.getPhosphorus() * ratio,
                vitamin);
    }

    public void updateRaw(Raw raw) {
        this.raw = raw;
    }
}
