package com.petplate.petplate.pet.domain;

import lombok.Getter;

public enum Neutering {
    INTACT(1.8), NEUTERED(1.6);

    @Getter
    private double value;

    Neutering(double value) {
        this.value = value;
    }
}
