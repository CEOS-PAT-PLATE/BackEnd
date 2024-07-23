package com.petplate.petplate.petdailymeal.repository;

import com.petplate.petplate.petdailymeal.domain.entity.DailyBookMarkedRaw;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DailyBookMarkedRawRepository extends JpaRepository<DailyBookMarkedRaw, Long> {
    List<DailyBookMarkedRaw> findByBookMarkedRawId(Long bookMarkedRawId);

    List<DailyBookMarkedRaw> findByDailyMealId(Long dailyMealId);
}
