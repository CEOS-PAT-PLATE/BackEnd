package com.petplate.petplate.dailyMealNutrient.service;

import com.petplate.petplate.common.EmbeddedType.StandardNutrient;
import com.petplate.petplate.common.response.error.ErrorCode;
import com.petplate.petplate.common.response.error.exception.BadRequestException;
import com.petplate.petplate.common.response.error.exception.InternalServerErrorException;
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
import com.petplate.petplate.utils.DailyMealUtil;
import com.petplate.petplate.utils.PetUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class SufficientNutrientService {
    private final DailyMealRepository dailyMealRepository;
    private final SufficientNutrientRepository sufficientNutrientRepository;
    private final PetRepository petRepository;

    @Transactional
    public void createSufficientNutrientsToday(String username, Long petId) {
        Pet pet = PetUtil.validUserAndFindPet(username, petId, petRepository);
        DailyMeal dailyMealToday = DailyMealUtil.findDailyMealToday(petId, dailyMealRepository);

        // 이미 과잉 영양소 생성했던 경우 기존의 과잉 영양소들을 제거하고 새로 분석함
        sufficientNutrientRepository.deleteAll(sufficientNutrientRepository.findByDailyMealId(dailyMealToday.getId()));

        double weight = pet.getWeight();
        Activity activity = pet.getActivity();
        Neutering neutering = pet.getNeutering();

        StandardNutrient.findSufficientNutrients(dailyMealToday.getNutrient(), weight, activity, neutering)
                .forEach(nutrient -> {
                    double amount = dailyMealToday.getNutrient().getNutrientAmountByName(nutrient.getName());
                    if (amount < 0.01) {
                        amount = 0;
                    }
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

                    // 알 수 없는 이유로 과잉 영양소가 중복 저장되는 경우
                    if (sufficientNutrientRepository.existsByDailyMealIdAndName(dailyMealToday.getId(), nutrient.getName())) {
                        log.error("중복 저장 과잉영양소: " + nutrient.getName() + " dailyMealId: " + dailyMealToday.getId());
                        throw new InternalServerErrorException(ErrorCode.SAME_SUFFICIENT_NUTRIENT_EXISTS);
                    }

                    sufficientNutrientRepository.save(sufficientNutrient);
                });

    }

    public List<ReadPetNutrientResponseDto> getSufficientNutrients(String username, Long petId, Long dailyMealId) {
        PetUtil.validUserAndFindPet(username, petId, petRepository);
        if (!dailyMealRepository.existsById(dailyMealId)) {
            throw new NotFoundException(ErrorCode.DAILY_MEAL_NOT_FOUND);
        }

        List<ReadPetNutrientResponseDto> responses = new ArrayList<>();
        sufficientNutrientRepository.findByDailyMealId(dailyMealId).forEach(sufficientNutrient -> {

            /**
             * 중복 조회 로직
             */
            boolean isDuplicate = responses.stream()
                    .anyMatch(response -> response.getName().equals(sufficientNutrient.getName()));

            if (!isDuplicate) {
                responses.add(
                        ReadPetNutrientResponseDto.of(
                                sufficientNutrient.getName(),
                                sufficientNutrient.getUnit(),
                                sufficientNutrient.getDescription(),
                                sufficientNutrient.getAmount(),
                                sufficientNutrient.getProperAmount(),
                                sufficientNutrient.getMaximumAmount())
                );
            }
        });

        return responses;
    }
}

