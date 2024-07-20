package com.petplate.petplate.utils;

import com.petplate.petplate.common.EmbeddedType.Nutrient;
import com.petplate.petplate.common.EmbeddedType.Vitamin;
import com.petplate.petplate.pet.domain.entity.Pet;
import com.petplate.petplate.petdailymeal.domain.entity.DailyMeal;
import com.petplate.petplate.petdailymeal.repository.DailyMealRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DailyMealUtil {
    private static final double HUDDLE = 0.01;

    /**
     * 음식 제거시 dailyMeal의 영양소가 소수점이 맞지 않아서 음수가 되는 문제가 발생함.
     * 따라서, 이를 보정하기 위해서 영양소가 음수가 되는 경우 영양소를 0으로 업데이트
     *
     * @param dailyMeal
     */
    public static void compensatingNutrient(DailyMeal dailyMeal) {
        Nutrient nutrient = dailyMeal.getNutrient();
        Vitamin vitamin = nutrient.getVitamin();

        Nutrient updatedNutrient = Nutrient.builder()
                .carbonHydrate(nutrient.getCarbonHydrate() < HUDDLE ? 0 : nutrient.getCarbonHydrate())
                .protein(nutrient.getProtein() < HUDDLE ? 0 : nutrient.getProtein())
                .fat(nutrient.getFat() < HUDDLE ? 0 : nutrient.getFat())
                .calcium(nutrient.getCalcium() < HUDDLE ? 0 : nutrient.getCalcium())
                .phosphorus(nutrient.getPhosphorus() < HUDDLE ? 0 : nutrient.getPhosphorus())
                .vitamin(Vitamin.builder()
                        .vitaminA(vitamin.getVitaminA() < HUDDLE ? 0 : vitamin.getVitaminA())
                        .vitaminD(vitamin.getVitaminD() < HUDDLE ? 0 : vitamin.getVitaminD())
                        .vitaminE(vitamin.getVitaminE() < HUDDLE ? 0 : vitamin.getVitaminE())
                        .build())
                .build();


        dailyMeal.updateNutrient(updatedNutrient);
    }

    public static DailyMeal getDailyMealToday(Pet pet, DailyMealRepository dailyMealRepository) {
        LocalDateTime startDatetime = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0));
        LocalDateTime endDatetime = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59));

        return dailyMealRepository.findByPetIdAndCreatedAtBetween(pet.getId(), startDatetime, endDatetime)
                .orElseGet(() -> dailyMealRepository.save(  // 없다면 새로운 엔티티를 생성한 후 반환
                        new DailyMeal(
                                Nutrient.builder()
                                        .carbonHydrate(0)
                                        .protein(0)
                                        .fat(0)
                                        .phosphorus(0)
                                        .calcium(0)
                                        .vitamin(Vitamin.builder()
                                                .vitaminA(0)
                                                .vitaminD(0)
                                                .vitaminE(0)
                                                .build())
                                        .build(),
                                pet,
                                0)
                ));
    }
}
