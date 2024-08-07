package com.petplate.petplate.pet.domain.entity;

import com.petplate.petplate.common.Inheritance.BaseEntity;
import com.petplate.petplate.pet.domain.Activity;
import com.petplate.petplate.pet.domain.Neutering;
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
    @Column(nullable = false)
    private Activity activity;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Neutering neutering;

    @Enumerated(EnumType.STRING)
    private ProfileImg profileImg;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    @Builder
    public Pet(String name, int age, double weight, Activity activity, Neutering neutering, User owner) {
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.activity = activity;
        this.neutering = neutering;
        this.profileImg = null;
        this.owner = owner;
    }

    public void updateInfo(String name, Integer age, Double weight, Activity activity, Neutering neutering) {
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
        if (neutering != null) {
            this.neutering = neutering;
        }
    }

    public void updateProfileImg(ProfileImg profileImg) {
        this.profileImg = profileImg;
    }

    public double getProperKcal() {
        return getProperKcal(this.weight, this.activity, this.neutering);
    }

    public static double getProperKcal(double weight, Activity activity, Neutering neutering) {
        double rer = 70 * Math.pow(weight, 0.75);  // 기초 대사량
        double activityValue = activity.getValue();  // 활동량
        double neuterValue = neutering.getValue();  // 중성화 여부

        double properKcal = rer * activityValue * neuterValue;
        return properKcal;
    }
}
