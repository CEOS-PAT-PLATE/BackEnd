package com.petplate.petplate.common.EmbeddedType;

import java.util.*;
import java.util.stream.Collectors;

import com.petplate.petplate.medicalcondition.domain.entity.Disease;
import com.petplate.petplate.pet.domain.Activity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public enum StandardNutrient {
    CARBON_HYDRATE("탄수화물", 0.07 / 4, 1.5, "g", "탄수화물에 대한 설명"
            , Arrays.asList(NutrientDisease.create("감기", "추워요"), NutrientDisease.create("독감", "많이 추워요"))
            , Arrays.asList(NutrientDisease.create("뼈삭음", "아파요"), NutrientDisease.create("암", "많이 아파요"))),
    PROTEIN("단백질", 4.95, 2.0, "g", "단백질에 대한 설명"
            , Arrays.asList(NutrientDisease.create("감기", "추워요"), NutrientDisease.create("독감", "많이 추워요"))
            , Arrays.asList(NutrientDisease.create("뼈삭음", "아파요"), NutrientDisease.create("암", "많이 아파요"))),
    FAT("지방", 1.51, 1.75, "g", "지방에 대한 설명",
            Arrays.asList(NutrientDisease.create("감기", "추워요"), NutrientDisease.create("독감", "많이 추워요")),
            Arrays.asList(NutrientDisease.create("뼈삭음", "아파요"), NutrientDisease.create("암", "많이 아파요"))),
    CALCIUM("칼슘", 0.14, 2.0, "g", "칼슘에 대한 설명",
            Arrays.asList(NutrientDisease.create("감기", "추워요"), NutrientDisease.create("독감", "많이 추워요")),
            Arrays.asList(NutrientDisease.create("뼈삭음", "아파요"), NutrientDisease.create("암", "많이 아파요"))),
    PHOSPHORUS("인", 0.11, 2.0, "g", "인에 대한 설명",
            Arrays.asList(NutrientDisease.create("감기", "추워요"), NutrientDisease.create("독감", "많이 추워요")),
            Arrays.asList(NutrientDisease.create("뼈삭음", "아파요"), NutrientDisease.create("암", "많이 아파요"))),
    VITAMIN_A("비타민A", 167.0, 5, "iu", "비타민A에 대한 설명",
            Arrays.asList(NutrientDisease.create("감기", "추워요"), NutrientDisease.create("독감", "많이 추워요")),
            Arrays.asList(NutrientDisease.create("뼈삭음", "아파요"), NutrientDisease.create("암", "많이 아파요"))), // 레티놀
    VITAMIN_B("비타민B", 0.041, 10, "mg", "비타민B에 대한 설명",
            Arrays.asList(NutrientDisease.create("감기", "추워요"), NutrientDisease.create("독감", "많이 추워요")),
            Arrays.asList(NutrientDisease.create("뼈삭음", "아파요"), NutrientDisease.create("암", "많이 아파요"))),  // b6 + b12
    VITAMIN_D("비타민D", 15.20, 10, "iu", "비타민D에 대한 설명",
            Arrays.asList(NutrientDisease.create("감기", "추워요"), NutrientDisease.create("독감", "많이 추워요")),
            Arrays.asList(NutrientDisease.create("뼈삭음", "아파요"), NutrientDisease.create("암", "많이 아파요"))),
    VITAMIN_E("비타민E", 1.00, 10, "iu", "비타민E에 대한 설명",
            Arrays.asList(NutrientDisease.create("감기", "추워요"), NutrientDisease.create("독감", "많이 추워요")),
            Arrays.asList(NutrientDisease.create("뼈삭음", "아파요"), NutrientDisease.create("암", "많이 아파요")));

    private String name;
    private double properAmountUnit;
    private double upperRange;  // 정상 허용 범위 (+)
    private String unit;
    private String description;
    private List<NutrientDisease> deficientCauseDisease;
    private List<NutrientDisease> sufficientCauseDisease;

    StandardNutrient(String name, double properAmountUnit, double upperRange, String unit, String description, List<NutrientDisease> deficientCauseDisease, List<NutrientDisease> sufficientCauseDisease) {
        this.name = name;
        this.properAmountUnit = properAmountUnit;
        this.upperRange = upperRange;
        this.unit = unit;
        this.description = description;
        this.deficientCauseDisease = deficientCauseDisease;
        this.sufficientCauseDisease = sufficientCauseDisease;
    }


    //가장 부족한 영양소 비율로 판단하기
    public static StandardNutrient findMostDeficientNutrient(Nutrient nutrient, double weight, Activity activity) {

        Map<StandardNutrient, Double> standardNutrientMap = getNutrientsMap(nutrient, weight, activity);

        List<Map.Entry<StandardNutrient, Double>> entries = standardNutrientMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toList());

        return entries.get(0).getKey();

    }

    //가장 영양소 과잉인 영양소 비율로 판단하기
    public static StandardNutrient findMostSufficientNutrient(Nutrient nutrient, double weight, Activity activity) {

        Map<StandardNutrient, Double> standardNutrientMap = getNutrientsMap(nutrient, weight, activity);

        List<Map.Entry<StandardNutrient, Double>> entries = standardNutrientMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toList());

        return entries.get(entries.size() - 1).getKey();

    }

    // 영양소들을 비율로 내림차순 하여 반환
    public static List<Map.Entry<StandardNutrient, Double>> getNutrientMapOrderByAmount(Nutrient nutrient, double weight, Activity activity) {
        Map<StandardNutrient, Double> nutrientsMap = getNutrientsMap(nutrient, weight, activity);

        return nutrientsMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toList());
    }

    public static Map<StandardNutrient, Double> getNutrientsMap(Nutrient nutrient, double weight, Activity activity) {

        Map<StandardNutrient, Double> standardNutrientMap = new HashMap<>();

        standardNutrientMap.put(CARBON_HYDRATE, nutrient.getCarbonHydrate() / calculateProperCarbonHydrateAmount(weight, activity));
        standardNutrientMap.put(FAT, nutrient.getFat() / calculateProperNutrientAmount(FAT, weight));
        standardNutrientMap.put(PROTEIN, nutrient.getProtein() / calculateProperNutrientAmount(PROTEIN, weight));
        standardNutrientMap.put(CALCIUM, nutrient.getCalcium() / calculateProperNutrientAmount(CALCIUM, weight));
        standardNutrientMap.put(PHOSPHORUS, nutrient.getPhosphorus() / calculateProperNutrientAmount(PHOSPHORUS, weight));
        standardNutrientMap.put(VITAMIN_A, nutrient.getVitamin().getVitaminA() / calculateProperNutrientAmount(VITAMIN_A, weight));
        standardNutrientMap.put(VITAMIN_B, nutrient.getVitamin().getVitaminB() / calculateProperNutrientAmount(VITAMIN_B, weight));
        standardNutrientMap.put(VITAMIN_D, nutrient.getVitamin().getVitaminD() / calculateProperNutrientAmount(VITAMIN_D, weight));
        standardNutrientMap.put(VITAMIN_E, nutrient.getVitamin().getVitaminE() / calculateProperNutrientAmount(VITAMIN_E, weight));

        return standardNutrientMap;
    }

    // 적정 탄수화물 계산을 위한 로직
    private static double calculateProperCarbonHydrateAmount(double weight, Activity activity) {
        // 적정 칼로리 계산
        double properKcal = activity.getProperKcal(weight);
        return CARBON_HYDRATE.getProperAmountUnit() * properKcal;
    }

    // 탄수화물 이외의 다른 영양소들의 적정 양을 계산 위한 로직
    private static double calculateProperNutrientAmount(StandardNutrient nutrient, double weight) {
        return nutrient.getProperAmountUnit() * calculateMetabolicWeight(weight);
    }

    // 기초대사량 계산
    private static double calculateMetabolicWeight(double weight) {
        return Math.pow(weight, 0.75);
    }

    @Getter
    public static class NutrientDisease {
        private String name;
        private String description;

        private static NutrientDisease create(String name, String description) {
            NutrientDisease nutrientDisease = new NutrientDisease();
            nutrientDisease.name = name;
            nutrientDisease.description = description;

            return nutrientDisease;
        }
    }
}
