package com.petplate.petplate.petdailymeal.service;

import com.petplate.petplate.common.EmbeddedType.Nutrient;
import com.petplate.petplate.common.EmbeddedType.Vitamin;
import com.petplate.petplate.common.response.error.ErrorCode;
import com.petplate.petplate.common.response.error.exception.BadRequestException;
import com.petplate.petplate.common.response.error.exception.NotFoundException;
import com.petplate.petplate.pet.domain.entity.Pet;
import com.petplate.petplate.pet.repository.PetRepository;
import com.petplate.petplate.petdailymeal.domain.entity.DailyFeed;
import com.petplate.petplate.petdailymeal.domain.entity.DailyMeal;
import com.petplate.petplate.petdailymeal.dto.request.CreateDailyFeedRequestDto;
import com.petplate.petplate.petdailymeal.dto.response.ReadDailyFeedResponseDto;
import com.petplate.petplate.petdailymeal.repository.DailyFeedRepository;
import com.petplate.petplate.petdailymeal.repository.DailyMealRepository;
import com.petplate.petplate.utils.DailyMealUtil;
import com.petplate.petplate.utils.PetUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DailyFeedService {
    private final DailyFeedRepository dailyFeedRepository;
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
     * dailyFeed를 오늘 식사에 추가
     *
     * @param username
     * @param petId
     * @param requestDto
     * @return
     */
    @Transactional
    public Long createDailyFeed(String username, Long petId, CreateDailyFeedRequestDto requestDto) {
        Pet pet = PetUtil.validUserAndFindPet(username, petId, petRepository);
        DailyMeal dailyMealToday = DailyMealUtil.getDailyMealToday(pet, dailyMealRepository);

        double serving = requestDto.getServing();

        DailyFeed dailyFeed = DailyFeed.builder()
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

        dailyFeedRepository.save(dailyFeed);


        // dailyMeal에 먹은만큼 칼로리, 영양소 추가
        dailyMealToday.addKcal(dailyFeed.getKcal());
        dailyMealToday.addNutrient(dailyFeed.getNutrient());

        return dailyFeed.getId();
    }

    private double calculateNutritionAmount(double serving, double nutritionPercent) {
        return serving * (nutritionPercent / 100);
    }

    /**
     * PK로 오늘 섭취한 DailyFeed 조회
     *
     * @param username
     * @param petId
     * @param dailyFeedId
     * @return
     */
    public ReadDailyFeedResponseDto getDailyFeed(String username, Long petId, Long dailyFeedId) {
        Pet pet = PetUtil.validUserAndFindPet(username, petId, petRepository);

        DailyFeed dailyFeed = dailyFeedRepository.findById(dailyFeedId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.DAILY_FEED_NOT_FOUND));

        if (!dailyFeed.getDailyMeal().getPet().getId().equals(pet.getId())) {
            throw new BadRequestException(ErrorCode.NOT_PET_FOOD);
        }

        return ReadDailyFeedResponseDto.from(dailyFeed);
    }

    /**
     * 오늘 섭취한 DailyFeed 제거
     *
     * @param username
     * @param petId
     * @param dailyFeedId
     */
    @Transactional
    public void deleteDailyFeed(String username, Long petId, Long dailyFeedId) {
        Pet pet = PetUtil.validUserAndFindPet(username, petId, petRepository);

        DailyFeed dailyFeed =
                dailyFeedRepository.findById(dailyFeedId)
                        .orElseThrow(() -> new NotFoundException(ErrorCode.DAILY_FEED_NOT_FOUND));

        if (!dailyFeed.getDailyMeal().getPet().getId().equals(pet.getId())) {
            throw new BadRequestException(ErrorCode.NOT_PET_FOOD);
        }

        // dailyMeal에서 삭제한 dailyFeed만큼의 영양분 제거
        DailyMeal dailyMeal = dailyFeed.getDailyMeal();
        dailyMeal.subtractKcal(dailyFeed.getKcal());
        dailyMeal.subtractNutrient(dailyFeed.getNutrient());

        // 영양소 보정
        DailyMealUtil.compensatingNutrient(dailyMeal);

        dailyFeedRepository.deleteById(dailyFeedId);
    }

    /**
     * 특정 dailyMeal의 모든 dailyFeed 제거
     *
     * @param username
     * @param petId
     * @param dailyMealId
     */
    @Transactional
    public void deleteDailyFeeds(String username, Long petId, Long dailyMealId) {
        Pet pet = PetUtil.validUserAndFindPet(username, petId, petRepository);

        DailyMeal dailyMeal = dailyMealRepository.findById(dailyMealId).orElseThrow(() ->
                new NotFoundException(ErrorCode.DAILY_MEAL_NOT_FOUND)
        );
        if (!dailyMeal.getPet().getId().equals(pet.getId())) {
            throw new BadRequestException(ErrorCode.NOT_PET_DAILY_MEAL);
        }

        List<DailyFeed> dailyFeeds = dailyFeedRepository.findByDailyMealId(dailyMealId);

        // 삭제한 만큼 dailyMeal에서 영양소 제거
        dailyFeeds.forEach(dailyFeed -> {
            dailyMeal.subtractKcal(dailyFeed.getKcal());
            dailyMeal.subtractNutrient(dailyFeed.getNutrient());
        });

        // 영양소 보정
        DailyMealUtil.compensatingNutrient(dailyMeal);

        // 전체 삭제
        dailyFeedRepository.deleteAll(dailyFeeds);
    }
}
