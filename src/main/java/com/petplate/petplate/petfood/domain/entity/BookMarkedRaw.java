package com.petplate.petplate.petfood.domain.entity;

import com.petplate.petplate.common.EmbeddedType.Nutrient;
import com.petplate.petplate.common.EmbeddedType.Vitamin;
import com.petplate.petplate.common.Inheritance.BaseEntity;
import com.petplate.petplate.user.domain.entity.User;
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
public class BookMarkedRaw extends BaseEntity{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_marked_raw_id")
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
    @JoinColumn(name = "raw_id", nullable = false)
    private Raw raw;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public BookMarkedRaw(double serving, Raw raw, User user) {
        this.serving = serving;
        this.name = raw.getName();
        this.raw = raw;
        this.serving = serving;

        double ratio = (serving / raw.getStandardAmount());

        this.kcal = raw.getKcal() * ratio;

        Nutrient rawNutrient = raw.getNutrient();
        Vitamin vitamin = new Vitamin(rawNutrient.getVitamin().getVitaminA() * ratio,
                rawNutrient.getVitamin().getVitaminD() * ratio,
                rawNutrient.getVitamin().getVitaminE() * ratio);
        this.nutrient = new Nutrient(rawNutrient.getCarbonHydrate()*ratio,
                rawNutrient.getProtein()*ratio,
                rawNutrient.getFat()*ratio,
                rawNutrient.getCalcium()*ratio,
                rawNutrient.getPhosphorus()*ratio,
                vitamin);
    }

    public void updateRaw(Raw raw) {
        this.raw = raw;
    }
}
