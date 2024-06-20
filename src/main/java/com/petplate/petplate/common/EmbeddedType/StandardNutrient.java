package com.petplate.petplate.common.EmbeddedType;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.Getter;

@Getter
public enum StandardNutrient {

    CARBON_HYDRATE("탄수화물", 120, "탄수화물에 대한 설명", Arrays.asList("감기", "뼈삭음", "암")),
    PROTEIN("단백질", 150, "단백질에 대한 설명", Arrays.asList("감기", "뼈삭음", "암")),
    FAT("지방", 200, "지방에 대한 설명", Arrays.asList("감기", "뼈삭음", "암"));

    private String name;
    private int todayProperAmount;

    private String description;

    private List<String> causeDisease;

    StandardNutrient(String name, int todayProperAmount, String description, List<String> causeDisease) {
        this.name = name;
        this.todayProperAmount = todayProperAmount;
        this.description = description;
        this.causeDisease = causeDisease;
    }

    public static List<Map.Entry<StandardNutrient, Double>> getStandardNutrientOrderByAmount(Nutrient nutrient) {
        Map<StandardNutrient, Double> nutrientsMap = getNutrientsMap(nutrient);

        return nutrientsMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toList());
    }

    //가장 부족한 영양소 비율로 판단하기
    public static StandardNutrient findDeficientNutrient(Nutrient nutrient) {

        Map<StandardNutrient, Double> standardNutrientMap = getNutrientsMap(nutrient);

        List<Map.Entry<StandardNutrient, Double>> entries = standardNutrientMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toList());

        return entries.get(0).getKey();

    }

    //가장 영양소 과잉인 영양소 비율로 판단하기

    public static StandardNutrient findSufficientNutrient(Nutrient nutrient) {

        Map<StandardNutrient, Double> standardNutrientMap = getNutrientsMap(nutrient);

        List<Map.Entry<StandardNutrient, Double>> entries = standardNutrientMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toList());

        return entries.get(entries.size() - 1).getKey();

    }

    private static Map<StandardNutrient, Double> getNutrientsMap(Nutrient nutrient) {

        Map<StandardNutrient, Double> standardNutrientMap = new HashMap<>();

        standardNutrientMap.put(FAT, nutrient.getFat() / FAT.todayProperAmount);
        standardNutrientMap.put(PROTEIN, nutrient.getProtein() / PROTEIN.todayProperAmount);
        standardNutrientMap.put(CARBON_HYDRATE, nutrient.getCarbonHydrate() / CARBON_HYDRATE.todayProperAmount);

        return standardNutrientMap;
    }


}
