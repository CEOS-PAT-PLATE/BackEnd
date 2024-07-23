package com.petplate.petplate.dailyMealNutrient.repository;

import com.petplate.petplate.dailyMealNutrient.domain.entity.DeficientNutrient;
import com.petplate.petplate.dailyMealNutrient.domain.entity.ProperNutrient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProperNutrientRepository extends JpaRepository<ProperNutrient, Long> {
    List<ProperNutrient> findByDailyMealId(Long dailyMealId);
    Optional<ProperNutrient> findByDailyMealIdAndName(Long dailyMealId, String name);
    boolean existsByDailyMealId(Long dailyMealId);
}
