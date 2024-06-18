package com.petplate.petplate.banner.domain.entity;

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
public class Banner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "banner_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false,length = 500)
    private String bannerImgPath;

    @Column(nullable = false)
    private String url;


    @Builder
    public Banner(String name, String bannerImgPath, String url) {
        this.name = name;
        this.bannerImgPath = bannerImgPath;
        this.url = url;
    }
}
