package com.petplate.petplate.petdailymeal.repository;

import com.petplate.petplate.petdailymeal.domain.entity.DailyPackagedSnack;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DailyPackagedSnackRepository extends CrudRepository<DailyPackagedSnack, Long> {
    List<DailyPackagedSnack> findByDailyMealId(Long dailyMealId);
}
