package com.petplate.petplate.dailyMealNutrient.repository;

import com.petplate.petplate.dailyMealNutrient.domain.entity.DeficientNutrient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface DeficientNutrientRepository extends JpaRepository<DeficientNutrient, Long> {
    List<DeficientNutrient> findByDailyMealId(Long dailyMealId);
    Optional<DeficientNutrient> findByDailyMealIdAndName(Long dailyMealId, String name);
    boolean existsByDailyMealIdAndName(Long dailyMealId, String name);
    boolean existsByDailyMealId(Long dailyMealId);
}
