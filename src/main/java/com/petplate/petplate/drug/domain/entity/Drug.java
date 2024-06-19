package com.petplate.petplate.drug.domain.entity;

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
public class Drug {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "drug_id")
    private Long id;

    @Column(nullable = false)
    private String name;


    private int cost;

    //영양성분 아직 작성안함

    @Column(length = 500)
    private String drugImgPath;

    @Column(length = 255)
    private String url;


    @Builder
    public Drug(String name, int cost, String drugImgPath, String url) {
        this.name = name;
        this.cost = cost;
        this.drugImgPath = drugImgPath;
        this.url = url;
    }
}
