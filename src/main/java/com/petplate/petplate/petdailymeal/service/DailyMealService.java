package com.petplate.petplate.petdailymeal.service;

import com.petplate.petplate.common.EmbeddedType.Nutrient;
import com.petplate.petplate.common.EmbeddedType.Vitamin;
import com.petplate.petplate.common.response.error.ErrorCode;
import com.petplate.petplate.common.response.error.exception.BadRequestException;
import com.petplate.petplate.common.response.error.exception.NotFoundException;
import com.petplate.petplate.pet.domain.entity.Pet;
import com.petplate.petplate.pet.repository.PetRepository;
import com.petplate.petplate.petdailymeal.domain.entity.DailyMeal;
import com.petplate.petplate.petdailymeal.dto.response.*;
import com.petplate.petplate.petdailymeal.repository.*;
import com.petplate.petplate.petdailymeal.repository.DailyFeedRepository;
import com.petplate.petplate.user.repository.UserRepository;
import com.petplate.petplate.utils.PetUtil;
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
public class DailyMealService {
    private final DailyMealRepository dailyMealRepository;
    private final PetRepository petRepository;
    private final DailyRawRepository dailyRawRepository;
    private final DailyFeedRepository dailyFeedRepository;
    private final DailyPackagedSnackRepository dailyPackagedSnackRepository;

    private final DailyBookMarkedRawRepository dailyBookMarkedRawRepository;
    private final DailyBookMarkedFeedRepository dailyBookMarkedFeedRepository;
    private final DailyBookMarkedPackagedSnackRepository dailyBookMarkedPackagedSnackRepository;

    @Transactional
    public DailyMeal createDailyMeal(String username, Long petId) {
        Pet pet = PetUtil.validUserAndFindPet(username, petId, petRepository);
        LocalDateTime startDatetime = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0));
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

    /**
     * 일자로 하루식사 조회
     *
     * @param username
     * @param petId
     * @param date
     * @return
     */
    public DailyMeal getDailyMealByDate(String username, Long petId, LocalDate date) {
        PetUtil.validUserAndFindPet(username, petId, petRepository);

        LocalDateTime startDatetime = LocalDateTime.of(date, LocalTime.of(0, 0, 0));
        LocalDateTime endDatetime = LocalDateTime.of(date, LocalTime.of(23, 59, 59));

        DailyMeal dailyMeal = dailyMealRepository.findByPetIdAndCreatedAtBetween(petId, startDatetime, endDatetime).orElseThrow(
                () -> new NotFoundException(ErrorCode.DAILY_MEAL_NOT_FOUND)
        );

        return dailyMeal;
    }

    /**
     * 특정 일자의 반려견의 식사 내역 반환
     *
     * @param username
     * @param petId
     * @param date
     * @return
     */
    public ReadDailyMealResponseDto getDailyMeal(String username, Long petId, LocalDate date) {
        PetUtil.validUserAndFindPet(username, petId, petRepository);

        LocalDateTime startDatetime = LocalDateTime.of(date, LocalTime.of(0, 0, 0));
        LocalDateTime endDatetime = LocalDateTime.of(date, LocalTime.of(23, 59, 59));

        DailyMeal dailyMeal = dailyMealRepository.findByPetIdAndCreatedAtBetween(petId, startDatetime, endDatetime).orElseThrow(
                () -> new NotFoundException(ErrorCode.DAILY_MEAL_NOT_FOUND)
        );

        return ReadDailyMealResponseDto.from(dailyMeal);
    }

    /**
     * 반려견의 모든 하루식사 내역 조회
     *
     * @param username
     * @param petId
     * @return
     */
    public List<ReadDailyMealResponseDto> getDailyMeals(String username, Long petId) {
        PetUtil.validUserAndFindPet(username, petId, petRepository);

        List<ReadDailyMealResponseDto> dailyMeals = new ArrayList<>();
        dailyMealRepository.findByPetIdOrderByCreatedAtDesc(petId).forEach(
                dailyMeal -> {
                    dailyMeals.add(ReadDailyMealResponseDto.from(dailyMeal));
                }
        );

        return dailyMeals;
    }

    /**
     * 특정 반려견의 식사 내역중 자연식만 반환
     *
     * @param username
     * @param petId
     * @param dailyMealId
     * @return
     */
    public List<ReadDailyRawResponseDto> getDailyRaws(String username, Long petId, Long dailyMealId) {
        PetUtil.validUserAndFindPet(username, petId, petRepository);

        dailyMealRepository.findById(dailyMealId).orElseThrow(
                () -> new NotFoundException(ErrorCode.DAILY_MEAL_NOT_FOUND)
        );

        List<ReadDailyRawResponseDto> responses = new ArrayList<>();
        dailyRawRepository.findByDailyMealId(dailyMealId).forEach(dailyRaw -> {
            responses.add(ReadDailyRawResponseDto.from(dailyRaw));
        });
        return responses;
    }

    /**
     * 특정 반려견의 식사 내역중 사료만 반환
     *
     * @param username
     * @param petId
     * @return
     */
    public List<ReadDailyFeedResponseDto> getDailyFeeds(String username, Long petId, Long dailyMealId) {
        PetUtil.validUserAndFindPet(username, petId, petRepository);

        dailyMealRepository.findById(dailyMealId).orElseThrow(
                () -> new NotFoundException(ErrorCode.DAILY_MEAL_NOT_FOUND)
        );

        List<ReadDailyFeedResponseDto> responses = new ArrayList<>();
        dailyFeedRepository.findByDailyMealId(dailyMealId).forEach(dailyFeed -> {
            responses.add(ReadDailyFeedResponseDto.from(dailyFeed));
        });

        return responses;
    }

    /**
     * 특정 반려견의 식사 내역중 포장간식만 반환
     *
     * @param username
     * @param petId
     * @return
     */
    public List<ReadDailyPackagedSnackResponseDto> getDailyPackagedSnacks(String username, Long petId, Long dailyMealId) {
        PetUtil.validUserAndFindPet(username, petId, petRepository);

        dailyMealRepository.findById(dailyMealId).orElseThrow(
                () -> new NotFoundException(ErrorCode.DAILY_MEAL_NOT_FOUND)
        );

        List<ReadDailyPackagedSnackResponseDto> responses = new ArrayList<>();
        dailyPackagedSnackRepository.findByDailyMealId(dailyMealId).forEach(dailyPackagedSnack -> {
            responses.add(ReadDailyPackagedSnackResponseDto.from(dailyPackagedSnack));
        });

        return responses;
    }

    /**
     * 특정 반려견의 식사 내역중 즐겨찾기 자연식만 반환
     *
     * @param username
     * @param petId
     * @return
     */
    public List<ReadDailyBookMarkedRawResponseDto> getDailyBookMarkedRaws(String username, Long petId, Long dailyMealId) {
        PetUtil.validUserAndFindPet(username, petId, petRepository);

        dailyMealRepository.findById(dailyMealId).orElseThrow(
                () -> new NotFoundException(ErrorCode.DAILY_MEAL_NOT_FOUND)
        );

        List<ReadDailyBookMarkedRawResponseDto> responses = new ArrayList<>();
        dailyBookMarkedRawRepository.findByDailyMealId(dailyMealId).forEach(dailyBookMarkedRaw -> {
            responses.add(ReadDailyBookMarkedRawResponseDto.from(dailyBookMarkedRaw));
        });

        return responses;
    }

    /**
     * 특정 반려견의 식사 내역중 즐겨찾기 사료만 반환
     *
     * @param username
     * @param petId
     * @return
     */
    public List<ReadDailyBookMarkedFeedResponseDto> getDailyBookMarkedFeeds(String username, Long petId, Long dailyMealId) {
        PetUtil.validUserAndFindPet(username, petId, petRepository);

        dailyMealRepository.findById(dailyMealId).orElseThrow(
                () -> new NotFoundException(ErrorCode.DAILY_MEAL_NOT_FOUND)
        );

        List<ReadDailyBookMarkedFeedResponseDto> responses = new ArrayList<>();
        dailyBookMarkedFeedRepository.findByDailyMealId(dailyMealId).forEach(dailyBookMarkedFeed -> {
            responses.add(ReadDailyBookMarkedFeedResponseDto.from(dailyBookMarkedFeed));
        });

        return responses;
    }

    /**
     * 특정 반려견의 식사 내역중 즐겨찾기 포장 간식만 반환
     *
     * @param username
     * @param petId
     * @return
     */
    public List<ReadDailyBookMarkedPackagedSnackResponseDto> getDailyBookMarkedPackagedSnacks(String username, Long petId, Long dailyMealId) {
        PetUtil.validUserAndFindPet(username, petId, petRepository);

        dailyMealRepository.findById(dailyMealId).orElseThrow(
                () -> new NotFoundException(ErrorCode.DAILY_MEAL_NOT_FOUND)
        );

        List<ReadDailyBookMarkedPackagedSnackResponseDto> responses = new ArrayList<>();
        dailyBookMarkedPackagedSnackRepository.findByDailyMealId(dailyMealId).forEach(dailyBookMarkedPackagedSnack -> {
            responses.add(ReadDailyBookMarkedPackagedSnackResponseDto.from(dailyBookMarkedPackagedSnack));
        });
        return responses;
    }

    /**
     * id로 조회하여 반려견의 식사 내역을 섭취 음식과 함께 반환
     *
     * @param username
     * @param petId
     * @return
     */
    public ReadDailyMealFoodResponseDto getDailyMealWithFoods(String username, Long petId, Long dailyMealId) {
        PetUtil.validUserAndFindPet(username, petId, petRepository);

        DailyMeal dailyMeal = dailyMealRepository.findById(dailyMealId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.DAILY_MEAL_NOT_FOUND));

        return ReadDailyMealFoodResponseDto.of(dailyMeal,
                dailyRawRepository.findByDailyMealId(dailyMeal.getId()),
                dailyFeedRepository.findByDailyMealId(dailyMeal.getId()),
                dailyPackagedSnackRepository.findByDailyMealId(dailyMeal.getId()),
                dailyBookMarkedRawRepository.findByDailyMealId(dailyMeal.getId()),
                dailyBookMarkedFeedRepository.findByDailyMealId(dailyMeal.getId()),
                dailyBookMarkedPackagedSnackRepository.findByDailyMealId(dailyMeal.getId())
        );
    }

    /**
     * 반려견의 모든 식사 내역을 섭취 음식과 함께 반환
     *
     * @param username
     * @param petId
     * @return
     */
    public List<ReadDailyMealFoodResponseDto> getDailyMealsWithAllFoods(String username, Long petId) {
        PetUtil.validUserAndFindPet(username, petId, petRepository);

        List<ReadDailyMealFoodResponseDto> response = new ArrayList<>();
        dailyMealRepository.findByPetIdOrderByCreatedAtDesc(petId).forEach(dailyMeal -> {

            response.add(ReadDailyMealFoodResponseDto.of(dailyMeal,
                            dailyRawRepository.findByDailyMealId(dailyMeal.getId()),
                            dailyFeedRepository.findByDailyMealId(dailyMeal.getId()),
                            dailyPackagedSnackRepository.findByDailyMealId(dailyMeal.getId()),
                            dailyBookMarkedRawRepository.findByDailyMealId(dailyMeal.getId()),
                            dailyBookMarkedFeedRepository.findByDailyMealId(dailyMeal.getId()),
                            dailyBookMarkedPackagedSnackRepository.findByDailyMealId(dailyMeal.getId())
                    )
            );
        });

        return response;
    }
}
