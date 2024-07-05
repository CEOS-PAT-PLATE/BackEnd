package com.petplate.petplate.petdailymeal.repository;

import com.petplate.petplate.petdailymeal.domain.entity.DailyFeed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DailyFeedRepository extends JpaRepository<DailyFeed, Long> {
    List<DailyFeed> findByDailyMealId(Long dailyMealId);
}
