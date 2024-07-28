package com.petplate.petplate.dailyMealNutrient.service;

import com.petplate.petplate.common.EmbeddedType.StandardNutrient;
import com.petplate.petplate.common.response.error.ErrorCode;
import com.petplate.petplate.common.response.error.exception.BadRequestException;
import com.petplate.petplate.common.response.error.exception.InternalServerErrorException;
import com.petplate.petplate.common.response.error.exception.NotFoundException;
import com.petplate.petplate.dailyMealNutrient.domain.entity.ProperNutrient;
import com.petplate.petplate.dailyMealNutrient.repository.ProperNutrientRepository;
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
public class ProperNutrientService {
    private final DailyMealRepository dailyMealRepository;
    private final ProperNutrientRepository properNutrientRepository;
    private final PetRepository petRepository;

    @Transactional
    public void createProperNutrientsToday(String username, Long petId) {
        Pet pet = PetUtil.validUserAndFindPet(username, petId, petRepository);
        DailyMeal dailyMealToday = DailyMealUtil.findDailyMealToday(petId, dailyMealRepository);

        // 이미 적정 영양소 생성했던 경우 기존의 적정 영양소들을 제거하고 새로 분석함
        properNutrientRepository.deleteAll(properNutrientRepository.findByDailyMealId(dailyMealToday.getId()));

        double weight = pet.getWeight();
        Activity activity = pet.getActivity();
        Neutering neutering = pet.getNeutering();

        StandardNutrient.findProperNutrients(dailyMealToday.getNutrient(), weight, activity, neutering)
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

                    ProperNutrient properNutrient = ProperNutrient.builder()
                            .name(nutrient.getName())
                            .unit(nutrient.getUnit())
                            .description(nutrient.getDescription())
                            .amount(amount)
                            .properAmount(properAmount)
                            .maximumAmount(maximumAmount)
                            .dailyMeal(dailyMealToday)
                            .build();

                    // 알 수 없는 이유로 적정 영양소가 중복 저장되는 경우
                    if (properNutrientRepository.existsByDailyMealIdAndName(dailyMealToday.getId(), nutrient.getName())) {
                        log.error("중복 저장 적정영양소: " + nutrient.getName() + " dailyMealId: " + dailyMealToday.getId());
                        throw new InternalServerErrorException(ErrorCode.SAME_PROPER_NUTRIENT_EXISTS);
                    }

                    properNutrientRepository.save(properNutrient);
                });

    }

    public List<ReadPetNutrientResponseDto> getProperNutrients(String username, Long petId, Long dailyMealId) {
        PetUtil.validUserAndFindPet(username, petId, petRepository);

        if (!dailyMealRepository.existsById(dailyMealId)) {
            throw new NotFoundException(ErrorCode.DAILY_MEAL_NOT_FOUND);
        }

        List<ReadPetNutrientResponseDto> responses = new ArrayList<>();
        properNutrientRepository.findByDailyMealId(dailyMealId).forEach(properNutrient -> {

            /**
             * 중복 조회 로직
             */
            boolean isDuplicate = responses.stream()
                    .anyMatch(response -> response.getName().equals(properNutrient.getName()));

            if (!isDuplicate) {
                responses.add(
                        ReadPetNutrientResponseDto.of(
                                properNutrient.getName(),
                                properNutrient.getUnit(),
                                properNutrient.getDescription(),
                                properNutrient.getAmount(),
                                properNutrient.getProperAmount(),
                                properNutrient.getMaximumAmount())
                );
            }
        });

        return responses;
    }
}
