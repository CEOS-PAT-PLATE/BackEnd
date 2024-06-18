package com.petplate.petplate.pet.domain.entity;

import com.petplate.petplate.common.Inheritance.BaseEntity;
import com.petplate.petplate.pet.domain.Activity;
import com.petplate.petplate.user.domain.entity.User;
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
public class Pet extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    private float weight;

    @Enumerated(EnumType.STRING)
    private Activity activity;

    @Column(nullable = false)
    private boolean isNeutering;


    private String profileImgPath;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User owner;

    @Builder
    public Pet(String name, int age, float weight, Activity activity, boolean isNeutering, User owner) {
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.activity = activity;
        this.isNeutering = isNeutering;
        this.profileImgPath = null;
        this.owner = owner;
    }
}
