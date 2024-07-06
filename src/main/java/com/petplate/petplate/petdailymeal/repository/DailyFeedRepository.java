package com.petplate.petplate.petdailymeal.repository;

import com.petplate.petplate.petdailymeal.domain.entity.DailyFeed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DailyFeedRepository extends JpaRepository<DailyFeed, Long> {
    List<DailyFeed> findByDailyMealId(Long dailyMealId);
}
