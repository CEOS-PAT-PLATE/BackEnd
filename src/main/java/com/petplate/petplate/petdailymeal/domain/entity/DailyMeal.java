package com.petplate.petplate.petdailymeal.domain.entity;

import com.petplate.petplate.common.EmbeddedType.Nutrient;
import com.petplate.petplate.common.EmbeddedType.Vitamin;
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
    private double kcal;  // 하루 섭취 총 칼로리

    @Embedded
    private Nutrient nutrient;  // 하루 섭취 총 영양소

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    // 이미 펫이 해당 날짜에 DailyMeal이 존재하는 경우 생성되면 안됨.(서비스에서 해결)
    @Builder
    public DailyMeal(Nutrient nutrient, Pet pet, double kcal) {
        this.kcal = kcal;
        this.nutrient = nutrient;
        this.pet = pet;
    }

    // 기존 섭취하였었던 영양소에 추가된 영양소를 더해줌
    public void addNutrient(Nutrient nutrient) {
        this.nutrient = Nutrient.builder()
                .carbonHydrate(this.nutrient.getCarbonHydrate() + nutrient.getCarbonHydrate())
                .protein(this.nutrient.getProtein() + nutrient.getProtein())
                .fat(this.nutrient.getFat() + nutrient.getFat())
                .calcium(this.nutrient.getCalcium() + nutrient.getCalcium())
                .phosphorus(this.nutrient.getPhosphorus() + nutrient.getPhosphorus())
                .vitamin(Vitamin.builder()
                        .vitaminA(this.nutrient.getVitamin().getVitaminA() + nutrient.getVitamin().getVitaminA())
                        .vitaminD(this.nutrient.getVitamin().getVitaminD() + nutrient.getVitamin().getVitaminD())
                        .vitaminE(this.nutrient.getVitamin().getVitaminE() + nutrient.getVitamin().getVitaminE())
                        .build())
                .build();
    }

    // 기존 섭취하였었던 kcal에 추가된 kcal을 더해줌
    public void addKcal(double kcal) {
        this.kcal = this.kcal + kcal;
    }

    // 기존 섭취하였었던 영양소에서 제거된 식사내역의 영양소를 빼줌
    public void subtractNutrient(Nutrient nutrient) {
        this.nutrient = Nutrient.builder()
                .carbonHydrate(this.nutrient.getCarbonHydrate() - nutrient.getCarbonHydrate())
                .protein(this.nutrient.getProtein() - nutrient.getProtein())
                .fat(this.nutrient.getFat() - nutrient.getFat())
                .calcium(this.nutrient.getCalcium() - nutrient.getCalcium())
                .phosphorus(this.nutrient.getPhosphorus() - nutrient.getPhosphorus())
                .vitamin(Vitamin.builder()
                        .vitaminA(this.nutrient.getVitamin().getVitaminA() - nutrient.getVitamin().getVitaminA())
                        .vitaminD(this.nutrient.getVitamin().getVitaminD() - nutrient.getVitamin().getVitaminD())
                        .vitaminE(this.nutrient.getVitamin().getVitaminE() - nutrient.getVitamin().getVitaminE())
                        .build())
                .build();
    }

    // 기존 섭취하였었던 kcal에 제거된 식사내역의 kcal을 빼줌
    public void subtractKcal(double kcal) {
        this.kcal = this.kcal - kcal;
    }
}
