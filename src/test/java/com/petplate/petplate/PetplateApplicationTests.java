package com.petplate.petplate;

import com.petplate.petplate.common.EmbeddedType.Nutrient;
import com.petplate.petplate.common.EmbeddedType.StandardNutrient;
import com.petplate.petplate.common.EmbeddedType.Vitamin;
import com.petplate.petplate.pet.domain.Activity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@SpringBootTest
class PetplateApplicationTests {

    @Test
    @DisplayName("영양소 분량 테스트")
    void contextLoads() {
        Nutrient nutrient = Nutrient.builder()
                .carbonHydrate(110)
                .fat(1.51)
                .vitamin(Vitamin.builder().vitaminD(3.6).vitaminA(3.5).vitaminE(2.1).build())
                .phosphorus(1.4)
                .calcium(9.8)
                .protein(50)
                .build();

        /*List<Map.Entry<StandardNutrient, Double>> standardNutrientOrderByAmount = StandardNutrient.getNutrientMapOrderByAmount(nutrient, 1, Activity.CALM);
        for (Map.Entry<StandardNutrient, Double> standardNutrientDoubleEntry : standardNutrientOrderByAmount) {
            System.out.println("standardNutrientDoubleEntry = " + standardNutrientDoubleEntry.getKey().getName() + ", 적정량 대비 섭취 비율: " + standardNutrientDoubleEntry.getValue());
        }

        //가장 부족한 영양소 이름
        StandardNutrient deficientNutrient = StandardNutrient.findMostDeficientNutrient(nutrient, 5, Activity.ACTIVE);
        System.out.println(deficientNutrient.getName());

        //가장 많은 영양소 이름
        StandardNutrient sufficientNutrient = StandardNutrient.findMostSufficientNutrient(nutrient, 5, Activity.ACTIVE);
        System.out.println(sufficientNutrient.getName());*/

        LocalDateTime startDatetime = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0));
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endDatetime = LocalDateTime.of(LocalDate.now(), LocalTime.of(now.getHour(), now.getMinute(), now.getSecond()));

        System.out.println("startDatetime = " + startDatetime);
        System.out.println("endDatetime = " + endDatetime);

        LocalDateTime endDatetime1 = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59));
        System.out.println("endDatetime1 = " + endDatetime1);

        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
        System.out.println("startOfDay = " + startOfDay);
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        System.out.println("endOfDay = " + endOfDay);
    }

}
