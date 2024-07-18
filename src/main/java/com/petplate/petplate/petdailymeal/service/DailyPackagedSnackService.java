package com.petplate.petplate.petdailymeal.service;

import com.petplate.petplate.common.EmbeddedType.Nutrient;
import com.petplate.petplate.common.EmbeddedType.Vitamin;
import com.petplate.petplate.common.response.error.ErrorCode;
import com.petplate.petplate.common.response.error.exception.BadRequestException;
import com.petplate.petplate.common.response.error.exception.NotFoundException;
import com.petplate.petplate.pet.domain.entity.Pet;
import com.petplate.petplate.pet.repository.PetRepository;
import com.petplate.petplate.petdailymeal.domain.entity.DailyMeal;
import com.petplate.petplate.petdailymeal.domain.entity.DailyPackagedSnack;
import com.petplate.petplate.petdailymeal.dto.request.CreateDailyPackagedSnackRequestDto;
import com.petplate.petplate.petdailymeal.dto.response.ReadDailyPackagedSnackResponseDto;
import com.petplate.petplate.petdailymeal.repository.DailyMealRepository;
import com.petplate.petplate.petdailymeal.repository.DailyPackagedSnackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DailyPackagedSnackService {
    private final DailyPackagedSnackRepository dailyPackagedSnackRepository;
    private final PetRepository petRepository;
    private final DailyMealRepository dailyMealRepository;

    // 1 IU retinol = 0.3 mcg RAE
    static final double vitaminAIuRetinolPerGram = 3333333.3333333335; // == 10E6 / 0.3

    // 1 IU = 0.025 μg
    static final double vitaminDIuPerGram = 4.0E7; // == 10E6 / 0.025

    // 1 IU (natural) = 0.67 mg Vitamin E (as alpha-tocopherol)
    // 1 IU (synthetic) = 0.45 mg Vitamin E (as alpha-tocopherol)
    static final double vitaminEIuNaturalPerGram = 1492.5373134328358; // == 10E3 / 0.67

    /**
     * dailyPackagedSnack을 오늘 식사에 추가
     *
     * @param username
     * @param petId
     * @param requestDto
     * @return
     */
    @Transactional
    public Long createDailyPackagedSnack(String username, Long petId, CreateDailyPackagedSnackRequestDto requestDto) {
        Pet pet = validUserAndFindPet(username, petId);
        DailyMeal dailyMealToday = getDailyMealToday(pet);

        double serving = requestDto.getServing();

        DailyPackagedSnack dailyPackagedSnack = DailyPackagedSnack.builder()
                .dailyMeal(dailyMealToday)
                .name(requestDto.getName())
                .kcal(requestDto.getKcal())
                .serving(serving)
                .nutrient(Nutrient.builder()
                        .carbonHydrate(calculateNutritionAmount(serving, requestDto.getCarbonHydratePercent()))
                        .protein(calculateNutritionAmount(serving, requestDto.getProteinPercent()))
                        .fat(calculateNutritionAmount(serving, requestDto.getFatPercent()))
                        .calcium(calculateNutritionAmount(serving, requestDto.getCalciumPercent()))
                        .phosphorus(calculateNutritionAmount(serving, requestDto.getPhosphorusPercent()))
                        .vitamin(Vitamin.builder()
                                .vitaminA(vitaminAIuRetinolPerGram * calculateNutritionAmount(serving, requestDto.getVitaminAPercent()))
                                .vitaminD(vitaminDIuPerGram * calculateNutritionAmount(serving, requestDto.getVitaminDPercent()))
                                .vitaminE(vitaminEIuNaturalPerGram * calculateNutritionAmount(serving, requestDto.getVitaminEPercent()))
                                .build())
                        .build())
                .build();

        dailyPackagedSnackRepository.save(dailyPackagedSnack);


        // dailyMeal에 먹은만큼 칼로리, 영양소 추가
        dailyMealToday.addKcal(dailyPackagedSnack.getKcal());
        dailyMealToday.addNutrient(dailyPackagedSnack.getNutrient());

        return dailyPackagedSnack.getId();
    }

    private double calculateNutritionAmount(double serving, double nutritionPercent) {
        return serving * (nutritionPercent / 100);
    }

    /**
     * PK로 오늘 섭취한 dailyPackagedSnack 조회
     *
     * @param username
     * @param petId
     * @param dailyPackagedSnackId
     * @return
     */
    public ReadDailyPackagedSnackResponseDto getDailyPackagedSnack(String username, Long petId, Long dailyPackagedSnackId) {
        Pet pet = validUserAndFindPet(username, petId);

        DailyPackagedSnack dailyPackagedSnack = dailyPackagedSnackRepository.findById(dailyPackagedSnackId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.DAILY_PACKAGED_SNACK_NOT_FOUND));

        if (!dailyPackagedSnack.getDailyMeal().getPet().getId().equals(pet.getId())) {
            throw new BadRequestException(ErrorCode.NOT_PET_FOOD);
        }

        return ReadDailyPackagedSnackResponseDto.from(dailyPackagedSnack);
    }

    /**
     * 오늘 섭취한 DailyPackagedSnack 제거
     *
     * @param username
     * @param petId
     * @param dailyPackagedSnackId
     */
    @Transactional
    public void deleteDailyPackagedSnack(String username, Long petId, Long dailyPackagedSnackId) {
        Pet pet = validUserAndFindPet(username, petId);

        DailyPackagedSnack dailyPackagedSnack =
                dailyPackagedSnackRepository.findById(dailyPackagedSnackId)
                        .orElseThrow(() -> new NotFoundException(ErrorCode.DAILY_PACKAGED_SNACK_NOT_FOUND));

        if (!dailyPackagedSnack.getDailyMeal().getPet().getId().equals(pet.getId())) {
            throw new BadRequestException(ErrorCode.NOT_PET_FOOD);
        }

        // dailyMeal에서 삭제한 dailyPackagedSnack만큼의 영양분 제거
        DailyMeal dailyMeal = dailyPackagedSnack.getDailyMeal();
        dailyMeal.subtractKcal(dailyPackagedSnack.getKcal());
        dailyMeal.subtractNutrient(dailyPackagedSnack.getNutrient());

        dailyPackagedSnackRepository.deleteById(dailyPackagedSnackId);
    }

    /**
     * 특정 dailyMeal의 모든 dailyPackagedSnack 제거
     *
     * @param username
     * @param petId
     * @param dailyMealId
     */
    @Transactional
    public void deleteDailyPackagedSnacks(String username, Long petId, Long dailyMealId) {
        Pet pet = validUserAndFindPet(username, petId);

        DailyMeal dailyMeal = dailyMealRepository.findById(dailyMealId).orElseThrow(() ->
                new NotFoundException(ErrorCode.DAILY_MEAL_NOT_FOUND)
        );
        if (!dailyMeal.getPet().getId().equals(pet.getId())) {
            throw new BadRequestException(ErrorCode.NOT_PET_DAILY_MEAL);
        }

        List<DailyPackagedSnack> dailyPackagedSnacks = dailyPackagedSnackRepository.findByDailyMealId(dailyMealId);

        // 삭제한 만큼 dailyMeal에서 영양소 제거
        dailyPackagedSnacks.forEach(dailyPackagedSnack -> {
            dailyMeal.subtractKcal(dailyPackagedSnack.getKcal());
            dailyMeal.subtractNutrient(dailyPackagedSnack.getNutrient());
        });

        // 전체 삭제
        dailyPackagedSnackRepository.deleteAll(dailyPackagedSnacks);
    }

    private DailyMeal getDailyMealToday(Pet pet) {
        LocalDateTime startDatetime = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0));
        LocalDateTime endDatetime = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59));

        return dailyMealRepository.findByPetIdAndCreatedAtBetween(pet.getId(), startDatetime, endDatetime)
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
