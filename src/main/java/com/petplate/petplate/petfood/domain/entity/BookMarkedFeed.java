package com.petplate.petplate.petfood.domain.entity;

import com.petplate.petplate.common.EmbeddedType.Nutrient;
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
public class BookMarkedFeed extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_marked_feed_id")
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
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @Builder
    public BookMarkedFeed(String name, double serving, double kcal, Nutrient nutrient, User user) {
        this.name = name;
        this.serving = serving;
        this.kcal = kcal;
        this.nutrient = nutrient;
        this.user = user;
    }
}
