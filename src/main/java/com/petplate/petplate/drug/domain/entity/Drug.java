package com.petplate.petplate.drug.domain.entity;

import com.petplate.petplate.common.EmbeddedType.StandardNutrient;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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


    private String englishName;

    //현재는 String 이지만 나중에 확장 가능성있음.
    private String vendor;

    @Column(length = 500)
    private String drugImgPath;

    @Column(length = 255)
    private String url;


    @ElementCollection(targetClass = StandardNutrient.class)
    @JoinTable(name = "StandardNutrient",joinColumns = @JoinColumn(name = "drug_id"))
    @Column(name ="StandardNutrientName", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<StandardNutrient> efficientNutrient = new HashSet<>();

    @Builder
    public Drug(String name, String englishName, String vendor,String drugImgPath, String url,Set<StandardNutrient> efficientNutrient) {
        this.name = name;
        this.englishName = englishName;
        this.vendor = vendor;
        this.drugImgPath = drugImgPath;
        this.url = url;
        this.efficientNutrient = efficientNutrient;
    }
}
