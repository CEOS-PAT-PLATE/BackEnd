package com.petplate.petplate.dailyMealNutrient.service;

import com.petplate.petplate.common.EmbeddedType.StandardNutrient;
import com.petplate.petplate.common.response.error.ErrorCode;
import com.petplate.petplate.common.response.error.exception.BadRequestException;
import com.petplate.petplate.common.response.error.exception.NotFoundException;
import com.petplate.petplate.dailyMealNutrient.domain.entity.SufficientNutrient;
import com.petplate.petplate.dailyMealNutrient.repository.SufficientNutrientRepository;
import com.petplate.petplate.pet.domain.Activity;
import com.petplate.petplate.pet.domain.Neutering;
import com.petplate.petplate.pet.domain.entity.Pet;
import com.petplate.petplate.pet.dto.response.ReadPetNutrientResponseDto;
import com.petplate.petplate.pet.repository.PetRepository;
import com.petplate.petplate.petdailymeal.domain.entity.DailyMeal;
import com.petplate.petplate.petdailymeal.repository.DailyMealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SufficientNutrientService {
    private final DailyMealRepository dailyMealRepository;
    private final SufficientNutrientRepository sufficientNutrientRepository;
    private final PetRepository petRepository;

    @Transactional
    public void createSufficientNutrientsToday(String username, Long petId) {
        Pet pet = validUserAndFindPet(username, petId);
        DailyMeal dailyMealToday = findDailyMealToday(petId);

        // 이미 과잉 영양소 생성했던 경우 생성 안함
        if (sufficientNutrientRepository.existsByDailyMealId(dailyMealToday.getId())){
            throw new BadRequestException(ErrorCode.NUTRIENT_ARLEADY_EXIST);
        }

        double weight = pet.getWeight();
        Activity activity = pet.getActivity();
        Neutering neutering = pet.getNeutering();

        StandardNutrient.findSufficientNutrients(dailyMealToday.getNutrient(), weight, activity, neutering)
                .forEach(nutrient -> {
                    double amount = dailyMealToday.getNutrient().getNutrientAmountByName(nutrient.getName());
                    double properAmount = StandardNutrient.calculateProperNutrientAmount(nutrient, weight);
                    double maximumAmount = StandardNutrient.calculateProperMaximumNutrientAmount(nutrient, weight);

                    if (nutrient.equals(StandardNutrient.CARBON_HYDRATE)) {
                        properAmount = StandardNutrient.calculateProperCarbonHydrateAmount(weight, activity, neutering);
                        maximumAmount = StandardNutrient.calculateProperMaximumCarbonHydrateAmount(weight, activity, neutering);
                    }

                    SufficientNutrient sufficientNutrient = SufficientNutrient.builder()
                            .name(nutrient.getName())
                            .unit(nutrient.getUnit())
                            .description(nutrient.getDescription())
                            .amount(amount)
                            .properAmount(properAmount)
                            .maximumAmount(maximumAmount)
                            .dailyMeal(dailyMealToday)
                            .build();

                    sufficientNutrientRepository.save(sufficientNutrient);
                });

    }

    public List<ReadPetNutrientResponseDto> getSufficientNutrients(String username, Long petId, Long dailyMealId) {
        validUserAndFindPet(username, petId);
        if (!dailyMealRepository.existsById(dailyMealId)) {
            throw new NotFoundException(ErrorCode.DAILY_MEAL_NOT_FOUND);
        }

        List<ReadPetNutrientResponseDto> responses = new ArrayList<>();
        sufficientNutrientRepository.findByDailyMealId(dailyMealId).forEach(sufficientNutrient -> {
            responses.add(
                    ReadPetNutrientResponseDto.of(
                            sufficientNutrient.getName(),
                            sufficientNutrient.getUnit(),
                            sufficientNutrient.getDescription(),
                            sufficientNutrient.getAmount(),
                            sufficientNutrient.getProperAmount(),
                            sufficientNutrient.getMaximumAmount())
            );
        });

        return responses;
    }

    private DailyMeal findDailyMealToday(Long petId) {
        return findDailyMeal(petId, LocalDate.now());
    }

    private DailyMeal findDailyMeal(Long petId, LocalDate date) {
        LocalDateTime startDatetime = LocalDateTime.of(date.minusDays(1), LocalTime.of(0, 0, 0));
        LocalDateTime endDatetime = LocalDateTime.of(date, LocalTime.of(23, 59, 59));

        DailyMeal dailyMeal = dailyMealRepository.findByPetIdAndCreatedAtBetween(petId, startDatetime, endDatetime)
                .orElseThrow(() -> new NotFoundException(ErrorCode.DAILY_MEAL_NOT_FOUND));
        return dailyMeal;
    }

    private Pet validUserAndFindPet(String username, Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PET_NOT_FOUND));

        // 조회하려는 반려견이 본인의 반려견이 아닌 경우 예외 발생
        if (!pet.getOwner().getUsername().equals(username)) {
            throw new BadRequestException(ErrorCode.NOT_USER_PET);
        }

        return pet;
    }
}

