package com.petplate.petplate.petdailymeal.repository;

import com.petplate.petplate.petdailymeal.domain.entity.DailyBookMarkedFeed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DailyBookMarkedFeedRepository extends JpaRepository<DailyBookMarkedFeed, Long> {
    List<DailyBookMarkedFeed> findByDailyMealId(Long dailyMealId);

    List<DailyBookMarkedFeed> findByBookMarkedFeedId(Long bookMarkedFeedId);
}
