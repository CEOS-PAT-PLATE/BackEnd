package com.petplate.petplate.petfood.repository;

import com.petplate.petplate.petfood.domain.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedRepository extends JpaRepository<Feed, Long> {
    List<Feed> findByDailyMealId(Long dailyMealId);
}
