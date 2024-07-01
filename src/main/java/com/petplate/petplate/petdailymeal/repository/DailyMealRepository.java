package com.petplate.petplate.petdailymeal.repository;

import com.petplate.petplate.petdailymeal.domain.entity.DailyMeal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface DailyMealRepository extends JpaRepository<DailyMeal, Long> {
    Optional<DailyMeal> findByPetIdAndCreatedAtBetween(Long petId, LocalDateTime start, LocalDateTime end);
    boolean existsByPetIdAndCreatedAtBetween(Long petId, LocalDateTime start, LocalDateTime end);
}
