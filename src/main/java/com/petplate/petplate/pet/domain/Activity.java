package com.petplate.petplate.pet.domain;

import lombok.Getter;

public enum Activity {
    INACTIVE(1.0),
    SOMEWHAT_ACTIVE(1.2),
    ACTIVE(1.4),
    VERY_ACTIVE(1.6);


    @Getter
    private double value;

    Activity(double value) {
        this.value = value;
    }
}
