package com.petplate.petplate.dailyMealNutrient.repository;

import com.petplate.petplate.dailyMealNutrient.domain.entity.ProperNutrient;
import com.petplate.petplate.dailyMealNutrient.domain.entity.SufficientNutrient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface SufficientNutrientRepository extends JpaRepository<SufficientNutrient, Long> {
    List<SufficientNutrient> findByDailyMealId(Long dailyMealId);
    Optional<SufficientNutrient> findByDailyMealIdAndName(Long dailyMealId, String name);
    boolean existsByDailyMealId(Long dailyMealId);

    boolean existsByDailyMealIdAndName(Long dailyMealId, String name);
}
