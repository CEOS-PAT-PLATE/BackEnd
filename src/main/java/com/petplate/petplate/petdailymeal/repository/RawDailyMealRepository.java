package com.petplate.petplate.petdailymeal.repository;

import com.petplate.petplate.petdailymeal.domain.entity.RawDailyMeal;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RawDailyMealRepository extends JpaRepository<RawDailyMeal, Long> {

    @EntityGraph(attributePaths = {"dailyMeal"})
    Optional<RawDailyMeal> findById(Long id);

    @EntityGraph(attributePaths = {"raw"})
    List<RawDailyMeal> findByDailyMealId(Long dailyMealId);

    @EntityGraph(attributePaths = {"raw"})
    List<RawDailyMeal> findAllByDailyMealCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    void deleteByDailyMealId(Long dailyMealId);
}
