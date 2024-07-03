package com.petplate.petplate.petdailymeal.service;

import com.petplate.petplate.common.EmbeddedType.Nutrient;
import com.petplate.petplate.common.EmbeddedType.Vitamin;
import com.petplate.petplate.common.response.error.ErrorCode;
import com.petplate.petplate.common.response.error.exception.BadRequestException;
import com.petplate.petplate.common.response.error.exception.NotFoundException;
import com.petplate.petplate.pet.domain.entity.Pet;
import com.petplate.petplate.pet.repository.PetRepository;
import com.petplate.petplate.petdailymeal.domain.entity.DailyMeal;
import com.petplate.petplate.petdailymeal.repository.DailyMealRepository;
import com.petplate.petplate.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DailyMealService {
    private final DailyMealRepository dailyMealRepository;
    private final PetRepository petRepository;
    private final UserRepository userRepository;

    public DailyMeal createDailyMeal(String username, Long petId) {
        Pet pet = findPet(username, petId);
        LocalDateTime startDatetime = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(0, 0, 0));
        LocalDateTime endDatetime = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59));

        // 이미 그날의 하루식사가 생성되어 있는 경우 -> 이미 존재하는 엔티티를 반환
        return dailyMealRepository.findByPetIdAndCreatedAtBetween(petId, startDatetime, endDatetime)
                .orElseGet(() -> dailyMealRepository.save(  // 없다면 새로운 엔티티를 생성한 후 반환
                        new DailyMeal(
                                Nutrient.builder()
                                        .carbonHydrate(0)
                                        .protein(0)
                                        .fat(0)
                                        .phosphorus(0)
                                        .calcium(0)
                                        .vitamin(Vitamin.builder()
                                                .vitaminA(0)
                                                .vitaminD(0)
                                                .vitaminE(0)
                                                .build())
                                        .build(),
                                pet,
                                0)
                ));
    }

    public DailyMeal getDailyMeal(String username, Long petId, LocalDate date) {
        findPet(username, petId);

        LocalDateTime startDatetime = LocalDateTime.of(date.minusDays(1), LocalTime.of(0, 0, 0));
        LocalDateTime endDatetime = LocalDateTime.of(date, LocalTime.of(23, 59, 59));

        return dailyMealRepository.findByPetIdAndCreatedAtBetween(petId, startDatetime, endDatetime).orElseThrow(
                () -> new NotFoundException(ErrorCode.DAILY_MEAL_NOT_FOUND)
        );
    }


    private Pet findPet(String username, Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PET_NOT_FOUND));

        // 조회하려는 반려견이 본인의 반려견이 아닌 경우 예외 발생
        if (!pet.getOwner().getUsername().equals(username)) {
            throw new BadRequestException(ErrorCode.NOT_USER_PET);
        }

        return pet;
    }
}
