package com.petplate.petplate.petdailymeal.repository;

import com.petplate.petplate.petdailymeal.domain.entity.DailyBookMarkedPackagedSnack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public interface DailyBookMarkedPackagedSnackRepository extends JpaRepository<DailyBookMarkedPackagedSnack, Long> {
    List<DailyBookMarkedPackagedSnack> findByDailyMealId(Long dailyMealId);
    List<DailyBookMarkedPackagedSnack> findByBookMarkedPackagedSnackId(Long bookMarkedSnackId);
}
