package com.petplate.petplate.medicalcondition.domain.entity;

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
public class Disease {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "disease_id")
    private Long id;

    @Column(length = 50,nullable = false, unique = true)
    private String name;

    @Column(length = 500,nullable = false)
    private String description;

    @Builder
    public Disease(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
