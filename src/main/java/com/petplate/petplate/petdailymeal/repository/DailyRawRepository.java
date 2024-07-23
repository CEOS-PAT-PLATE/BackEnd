package com.petplate.petplate.petdailymeal.repository;

import com.petplate.petplate.petdailymeal.domain.entity.DailyRaw;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DailyRawRepository extends JpaRepository<DailyRaw, Long> {

    @EntityGraph(attributePaths = {"dailyMeal"})
    Optional<DailyRaw> findById(Long id);

    @EntityGraph(attributePaths = {"raw"})
    List<DailyRaw> findByDailyMealId(Long dailyMealId);

    @EntityGraph(attributePaths = {"raw"})
    List<DailyRaw> findAllByDailyMealCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    void deleteByDailyMealId(Long dailyMealId);

    List<DailyRaw> findByRawId(Long rawId);
}
