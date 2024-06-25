package com.petplate.petplate.pet.domain;

public enum Activity {
    ACTIVE(125),
    NORMAL(110),
    CALM(90);

    private double properKcalPerWeight;

    Activity(double properKcalPerWeight) {
        this.properKcalPerWeight = properKcalPerWeight;
    }

    // 적정 섭취 칼로리 반환
    public double getProperKcal(double weight) {
        return this.properKcalPerWeight * weight;
    }

    // 적정 섭취 칼로리 대비 실제 섭취 칼로리에 대한 비율 반환
    public double getProperKcalRatio(double weight, double kcal) {
        return kcal / this.properKcalPerWeight * weight;
    }
}
