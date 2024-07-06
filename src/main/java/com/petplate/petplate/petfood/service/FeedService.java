package com.petplate.petplate.petfood.service;

import com.petplate.petplate.common.EmbeddedType.Nutrient;
import com.petplate.petplate.common.EmbeddedType.Vitamin;
import com.petplate.petplate.common.response.error.ErrorCode;
import com.petplate.petplate.common.response.error.exception.BadRequestException;
import com.petplate.petplate.common.response.error.exception.NotFoundException;
import com.petplate.petplate.pet.domain.entity.Pet;
import com.petplate.petplate.pet.repository.PetRepository;
import com.petplate.petplate.petfood.domain.entity.Feed;
import com.petplate.petplate.petdailymeal.domain.entity.DailyMeal;
import com.petplate.petplate.petfood.dto.request.CreateFeedRequestDto;
import com.petplate.petplate.petfood.dto.response.ReadFeedResponseDto;
import com.petplate.petplate.petfood.repository.FeedRepository;
import com.petplate.petplate.petdailymeal.repository.DailyMealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FeedService {
    private final FeedRepository feedRepository;
    private final PetRepository petRepository;
    private final DailyMealRepository dailyMealRepository;

    // 1 IU retinol = 0.3 mcg RAE
    static final double vitaminAIuRetinolPerGram = 3333333.3333333335; // == 10E6 / 0.3

    // 1 IU = 0.025 μg
    static final double vitaminDIuRetinolPerGram = 4.0E7; // == 10E6 / 0.025

    // 1 IU (natural) = 0.67 mg Vitamin E (as alpha-tocopherol)
    // 1 IU (synthetic) = 0.45 mg Vitamin E (as alpha-tocopherol)
    static final double vitaminEIuNaturalPerGram = 1492.5373134328358; // == 10E3 / 0.67

    /**
     * Feed를 오늘 식사에 추가
     * @param username
     * @param petId
     * @param requestDto
     * @return
     */
    @Transactional
    public Long createDailyFeed(String username, Long petId, CreateFeedRequestDto requestDto) {
        Pet pet = validUserAndFindPet(username, petId);
        DailyMeal dailyMealToday = getDailyMealToday(pet);

        double serving = requestDto.getServing();

        Feed feed = Feed.builder()
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
                                .vitaminD(vitaminDIuRetinolPerGram * calculateNutritionAmount(serving, requestDto.getVitaminDPercent()))
                                .vitaminE(vitaminEIuNaturalPerGram * calculateNutritionAmount(serving, requestDto.getVitaminEPercent()))
                                .build())
                        .build())
                .build();

        feedRepository.save(feed);


        // dailyMeal에 먹은만큼 칼로리, 영양소 추가
        dailyMealToday.addKcal(feed.getKcal());
        dailyMealToday.addNutrient(feed.getNutrient());

        return feed.getId();
    }

    private double calculateNutritionAmount(double serving, double nutritionPercent) {
        return serving * (nutritionPercent / 100);
    }

    /**
     * PK로 오늘 섭취한 Feed 조회
     *
     * @param username
     * @param petId
     * @param feedId
     * @return
     */
    public ReadFeedResponseDto getDailyFeed(String username, Long petId, Long feedId) {
        Pet pet = validUserAndFindPet(username, petId);

        Feed feed = feedRepository.findById(feedId).orElseThrow(() -> new NotFoundException(ErrorCode.DAILY_FEED_NOT_FOUND));

        if (!feed.getDailyMeal().getPet().getId().equals(pet.getId())) {
            throw new BadRequestException(ErrorCode.NOT_PET_FOOD);
        }

        return ReadFeedResponseDto.from(feed);
    }

    /**
     * 오늘 섭취한 Feed 제거
     * @param username
     * @param petId
     * @param feedId
     */
    @Transactional
    public void deleteDailyFeed(String username, Long petId, Long feedId) {
        Pet pet = validUserAndFindPet(username, petId);

        Feed feed =
                feedRepository.findById(feedId)
                        .orElseThrow(() -> new NotFoundException(ErrorCode.DAILY_FEED_NOT_FOUND));

        if(!feed.getDailyMeal().getPet().getId().equals(pet.getId())) {
            throw new BadRequestException(ErrorCode.NOT_PET_FOOD);
        }

        // dailyMeal에서 삭제한 dailyFeed만큼의 영양분 제거
        DailyMeal dailyMeal = feed.getDailyMeal();
        dailyMeal.subtractKcal(feed.getKcal());
        dailyMeal.subtractNutrient(feed.getNutrient());

        feedRepository.deleteById(feedId);
    }

    private DailyMeal getDailyMealToday(Pet pet) {
        LocalDateTime startDatetime = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(0, 0, 0));
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
