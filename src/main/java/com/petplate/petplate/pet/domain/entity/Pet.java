package com.petplate.petplate.pet.domain.entity;

import com.petplate.petplate.common.EmbeddedType.Nutrient;
import com.petplate.petplate.common.Inheritance.BaseEntity;
import com.petplate.petplate.pet.domain.Activity;
import com.petplate.petplate.pet.domain.ProfileImg;
import com.petplate.petplate.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(exclude = {"owner"})
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
    private double weight;

    @Enumerated(EnumType.STRING)
    private Activity activity;

    @Column(nullable = false)
    private boolean isNeutering;

    private ProfileImg profileImg;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    @Builder
    public Pet(String name, int age, double weight, Activity activity, boolean isNeutering, User owner) {
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.activity = activity;
        this.isNeutering = isNeutering;
        this.profileImg = null;
        this.owner = owner;
    }

    public void updateInfo(String name, Integer age, Double weight, Activity activity, Boolean isNeutering) {
        if (name != null) {
            this.name = name;
        }
        if (age != null) {
            this.age = age;
        }
        if (weight != null) {
            this.weight = weight;
        }
        if (activity != null) {
            this.activity = activity;
        }
        if (isNeutering != null) {
            this.isNeutering = isNeutering;
        }
    }

    public void updateProfileImg(ProfileImg profileImg) {
        this.profileImg = profileImg;
    }
}
