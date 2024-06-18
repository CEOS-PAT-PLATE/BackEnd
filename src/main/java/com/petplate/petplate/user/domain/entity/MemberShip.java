package com.petplate.petplate.user.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberShip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_ship_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    //단위: 원
    @Column(nullable = false)
    private int cost;

    @Column(nullable = false)
    private int duration;

    @Builder
    public MemberShip(String name, int cost, int duration) {
        this.name = name;
        this.cost = cost;
        this.duration = duration;
    }
}
