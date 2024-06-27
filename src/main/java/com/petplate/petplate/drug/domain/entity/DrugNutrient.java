package com.petplate.petplate.drug.domain.entity;

import com.petplate.petplate.common.EmbeddedType.StandardNutrient;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class DrugNutrient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "drug_id",nullable = false)
    private Drug drug;

    @Enumerated(EnumType.STRING)
    @Column(name = "standard_nutrient",nullable = false)
    private StandardNutrient standardNutrient;



    @Builder
    public DrugNutrient(Drug drug, StandardNutrient standardNutrient){
        this.drug = drug;
        this.standardNutrient = standardNutrient;
    }

}
