package com.petplate.petplate.petdailymeal.repository;

import com.petplate.petplate.petdailymeal.domain.entity.DailyMeal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface DailyMealRepository extends JpaRepository<DailyMeal, Long> {
    Optional<DailyMeal> findById(Long mealId);
    Optional<DailyMeal> findByPetIdAndCreatedAt(Long petId, LocalDate createdAt);
    boolean existsByPetIdAndCreatedAt(Long petId, LocalDate createdAt);
}
