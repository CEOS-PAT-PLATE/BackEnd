package com.petplate.petplate.common.EmbeddedType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.Getter;

@Getter
public enum StandardNutrient {

//    CARBON_HYDRATE("탄수화물", 150, "g", "탄수화물에 대한 설명", Arrays.asList("감기", "뼈삭음", "암")),
    PROTEIN("단백질", 4.95, "g", "단백질에 대한 설명", Arrays.asList("감기", "뼈삭음", "암")),
    FAT("지방", 1.51, "g", "지방에 대한 설명", Arrays.asList("감기", "뼈삭음", "암")),
    CALCIUM("칼슘", 0.14, "g", "칼슘에 대한 설명", Arrays.asList("감기", "뼈삭음", "암")),
    PHOSPHORUS("인", 0.11, "g", "인에 대한 설명", Arrays.asList("감기", "뼈삭음", "암")),
    VITAMIN_A("비타민A", 50.1, "mcg RAE", "비타민A에 대한 설명", Arrays.asList("감기", "뼈삭음", "암")), // 레티놀
    VITAMIN_B("비타민B", 0.041, "mg", "비타민B에 대한 설명", Arrays.asList("감기", "뼈삭음", "암")),  // b6 + b12
    VITAMIN_D("비타민D", 0.38, "mcg", "비타민D에 대한 설명", Arrays.asList("감기", "뼈삭음", "암")),
    VITAMIN_E("비타민E", 0.025, "mcg", "비타민E에 대한 설명", Arrays.asList("감기", "뼈삭음", "암"));

    private String name;
    private double properAmountPerMetabolicWeight;
    private String unit;
    private String description;
    private List<String> causeDisease;

    StandardNutrient(String name, double properAmountPerMetabolicWeight, String unit, String description, List<String> causeDisease) {
        this.name = name;
        this.properAmountPerMetabolicWeight = properAmountPerMetabolicWeight;
        this.unit = unit;
        this.description = description;
        this.causeDisease = causeDisease;
    }

    public static List<Map.Entry<StandardNutrient, Double>> getStandardNutrientOrderByAmount(Nutrient nutrient, double weight) {
        Map<StandardNutrient, Double> nutrientsMap = getNutrientsMap(nutrient, weight);

        return nutrientsMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toList());
    }

    //가장 부족한 영양소 비율로 판단하기
    public static StandardNutrient findDeficientNutrient(Nutrient nutrient, double weight) {

        Map<StandardNutrient, Double> standardNutrientMap = getNutrientsMap(nutrient, weight);

        List<Map.Entry<StandardNutrient, Double>> entries = standardNutrientMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toList());

        return entries.get(0).getKey();

    }

    //가장 영양소 과잉인 영양소 비율로 판단하기

    public static StandardNutrient findSufficientNutrient(Nutrient nutrient, double weight) {

        Map<StandardNutrient, Double> standardNutrientMap = getNutrientsMap(nutrient, weight);

        List<Map.Entry<StandardNutrient, Double>> entries = standardNutrientMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toList());

        return entries.get(entries.size() - 1).getKey();

    }

    private static Map<StandardNutrient, Double> getNutrientsMap(Nutrient nutrient, double weight) {

        Map<StandardNutrient, Double> standardNutrientMap = new HashMap<>();

        standardNutrientMap.put(FAT, nutrient.getFat() / calculateProperFat(weight));
        standardNutrientMap.put(PROTEIN, nutrient.getProtein() / calculateProperProtein(weight));
        standardNutrientMap.put(CALCIUM, nutrient.getCalcium() / calculateProperCalcium(weight));
        standardNutrientMap.put(PHOSPHORUS, nutrient.getPhosphorus() / calculateProperPhosphorus(weight));
        standardNutrientMap.put(VITAMIN_A, nutrient.getVitamin().getVitaminA() / calculateProperVitaminA(weight));
        standardNutrientMap.put(VITAMIN_B, nutrient.getVitamin().getVitaminB() / calculateProperVitaminB(weight));
        standardNutrientMap.put(VITAMIN_D, nutrient.getVitamin().getVitaminD() / calculateProperVitaminD(weight));
        standardNutrientMap.put(VITAMIN_E, nutrient.getVitamin().getVitaminE() / calculateProperVitaminE(weight));

        return standardNutrientMap;
    }

    private static double calculateProperProtein(double weight) {
        return PROTEIN.getProperAmountPerMetabolicWeight() * calculateMetabolicWeight(weight);
    }

    private static double calculateProperFat(double weight) {
        return FAT.getProperAmountPerMetabolicWeight() * calculateMetabolicWeight(weight);
    }

    private static double calculateProperCalcium(double weight) {
        return CALCIUM.getProperAmountPerMetabolicWeight() * calculateMetabolicWeight(weight);
    }

    private static double calculateProperPhosphorus(double weight) {
        return PHOSPHORUS.getProperAmountPerMetabolicWeight() * calculateMetabolicWeight(weight);
    }

    private static double calculateProperVitaminA(double weight) {
        return VITAMIN_A.getProperAmountPerMetabolicWeight() * calculateMetabolicWeight(weight);
    }

    private static double calculateProperVitaminB(double weight) {
        return VITAMIN_B.getProperAmountPerMetabolicWeight() * calculateMetabolicWeight(weight);
    }

    private static double calculateProperVitaminD(double weight) {
        return VITAMIN_D.getProperAmountPerMetabolicWeight() * calculateMetabolicWeight(weight);
    }

    private static double calculateProperVitaminE(double weight) {
        return VITAMIN_E.getProperAmountPerMetabolicWeight() * calculateMetabolicWeight(weight);
    }

    // 기초대사량 계산
    private static double calculateMetabolicWeight(double weight) {
        return Math.pow(weight, 0.75);
    }

}
