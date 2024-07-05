package com.petplate.petplate.petdailymeal.service;

import com.petplate.petplate.common.EmbeddedType.Nutrient;
import com.petplate.petplate.common.EmbeddedType.Vitamin;
import com.petplate.petplate.common.response.error.ErrorCode;
import com.petplate.petplate.common.response.error.exception.BadRequestException;
import com.petplate.petplate.common.response.error.exception.NotFoundException;
import com.petplate.petplate.pet.domain.entity.Pet;
import com.petplate.petplate.pet.repository.PetRepository;
import com.petplate.petplate.petdailymeal.domain.entity.DailyMeal;
import com.petplate.petplate.petdailymeal.dto.response.ReadDailyMealResponseDto;
import com.petplate.petplate.petdailymeal.repository.*;
import com.petplate.petplate.user.repository.UserRepository;
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
    private final UserRepository userRepository;

    private final DailyRawRepository dailyRawRepository;
    private final DailyFeedRepository dailyFeedRepository;
    private final DailyPackagedSnackRepository dailyPackagedSnackRepository;

    private final DailyBookMarkedRawRepository dailyBookMarkedRawRepository;
    private final DailyBookMarkedFeedRepository dailyBookMarkedFeedRepository;
    private final DailyBookMarkedPackagedSnackRepository dailyBookMarkedPackagedSnackRepository;


    public DailyMeal createDailyMeal(String username, Long petId) {
        Pet pet = validUserAndFindPet(username, petId);
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

    public DailyMeal getDailyMealByDate(String username, Long petId, LocalDate date) {
        validUserAndFindPet(username, petId);

        LocalDateTime startDatetime = LocalDateTime.of(date.minusDays(1), LocalTime.of(0, 0, 0));
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
    public ReadDailyMealResponseDto getDailyMealWithAllFoods(String username, Long petId, LocalDate date) {
        validUserAndFindPet(username, petId);

        LocalDateTime startDatetime = LocalDateTime.of(date.minusDays(1), LocalTime.of(0, 0, 0));
        LocalDateTime endDatetime = LocalDateTime.of(date, LocalTime.of(23, 59, 59));

        DailyMeal dailyMeal = dailyMealRepository.findByPetIdAndCreatedAtBetween(petId, startDatetime, endDatetime).orElseThrow(
                () -> new NotFoundException(ErrorCode.DAILY_MEAL_NOT_FOUND)
        );

        return ReadDailyMealResponseDto.of(dailyMeal,
                dailyRawRepository.findByDailyMealId(dailyMeal.getId()),
                dailyFeedRepository.findByDailyMealId(dailyMeal.getId()),
                dailyPackagedSnackRepository.findByDailyMealId(dailyMeal.getId()),
                dailyBookMarkedRawRepository.findByDailyMealId(dailyMeal.getId()),
                dailyBookMarkedFeedRepository.findByDailyMealId(dailyMeal.getId()),
                dailyBookMarkedPackagedSnackRepository.findByDailyMealId(dailyMeal.getId())
        );
    }

    /**
     * 특정 일자의 반려견의 식사 내역중 자연식만 반환
     *
     * @param username
     * @param petId
     * @param date
     * @return
     */
    public ReadDailyMealResponseDto getDailyMealWithDailyRaws(String username, Long petId, LocalDate date) {
        validUserAndFindPet(username, petId);

        LocalDateTime startDatetime = LocalDateTime.of(date.minusDays(1), LocalTime.of(0, 0, 0));
        LocalDateTime endDatetime = LocalDateTime.of(date, LocalTime.of(23, 59, 59));

        DailyMeal dailyMeal = dailyMealRepository.findByPetIdAndCreatedAtBetween(petId, startDatetime, endDatetime).orElseThrow(
                () -> new NotFoundException(ErrorCode.DAILY_MEAL_NOT_FOUND)
        );

        return ReadDailyMealResponseDto.of(dailyMeal,
                dailyRawRepository.findByDailyMealId(dailyMeal.getId()),
                null,
                null,
                null,
                null,
                null
        );
    }

    /**
     * 특정 일자의 반려견의 식사 내역중 사료만 반환
     *
     * @param username
     * @param petId
     * @param date
     * @return
     */
    public ReadDailyMealResponseDto getDailyMealWithDailyFeeds(String username, Long petId, LocalDate date) {
        validUserAndFindPet(username, petId);

        LocalDateTime startDatetime = LocalDateTime.of(date.minusDays(1), LocalTime.of(0, 0, 0));
        LocalDateTime endDatetime = LocalDateTime.of(date, LocalTime.of(23, 59, 59));

        DailyMeal dailyMeal = dailyMealRepository.findByPetIdAndCreatedAtBetween(petId, startDatetime, endDatetime).orElseThrow(
                () -> new NotFoundException(ErrorCode.DAILY_MEAL_NOT_FOUND)
        );

        return ReadDailyMealResponseDto.of(dailyMeal,
                null,
                dailyFeedRepository.findByDailyMealId(dailyMeal.getId()),
                null,
                null,
                null,
                null
        );
    }

    /**
     * 특정 일자의 반려견의 식사 내역중 포장간식만 반환
     *
     * @param username
     * @param petId
     * @param date
     * @return
     */
    public ReadDailyMealResponseDto getDailyMealWithDailyPackagedSnacks(String username, Long petId, LocalDate date) {
        validUserAndFindPet(username, petId);

        LocalDateTime startDatetime = LocalDateTime.of(date.minusDays(1), LocalTime.of(0, 0, 0));
        LocalDateTime endDatetime = LocalDateTime.of(date, LocalTime.of(23, 59, 59));

        DailyMeal dailyMeal = dailyMealRepository.findByPetIdAndCreatedAtBetween(petId, startDatetime, endDatetime).orElseThrow(
                () -> new NotFoundException(ErrorCode.DAILY_MEAL_NOT_FOUND)
        );

        return ReadDailyMealResponseDto.of(dailyMeal,
                null,
                null,
                dailyPackagedSnackRepository.findByDailyMealId(dailyMeal.getId()),
                null,
                null,
                null
        );
    }

    /**
     * 특정 일자의 반려견의 식사 내역중 즐겨찾기 만 반환
     *
     * @param username
     * @param petId
     * @param date
     * @return
     */
    public ReadDailyMealResponseDto getDailyMealWithDailyBookMarkedRaws(String username, Long petId, LocalDate date) {
        validUserAndFindPet(username, petId);

        LocalDateTime startDatetime = LocalDateTime.of(date.minusDays(1), LocalTime.of(0, 0, 0));
        LocalDateTime endDatetime = LocalDateTime.of(date, LocalTime.of(23, 59, 59));

        DailyMeal dailyMeal = dailyMealRepository.findByPetIdAndCreatedAtBetween(petId, startDatetime, endDatetime).orElseThrow(
                () -> new NotFoundException(ErrorCode.DAILY_MEAL_NOT_FOUND)
        );

        return ReadDailyMealResponseDto.of(dailyMeal,
                null,
                null,
                null,
                dailyBookMarkedRawRepository.findByDailyMealId(dailyMeal.getId()),
                null,
                null
        );
    }

    /**
     * 특정 일자의 반려견의 식사 내역중 즐겨찾기 사료만 반환
     *
     * @param username
     * @param petId
     * @param date
     * @return
     */
    public ReadDailyMealResponseDto getDailyMealWithDailyBookMarkedFeeds(String username, Long petId, LocalDate date) {
        validUserAndFindPet(username, petId);

        LocalDateTime startDatetime = LocalDateTime.of(date.minusDays(1), LocalTime.of(0, 0, 0));
        LocalDateTime endDatetime = LocalDateTime.of(date, LocalTime.of(23, 59, 59));

        DailyMeal dailyMeal = dailyMealRepository.findByPetIdAndCreatedAtBetween(petId, startDatetime, endDatetime).orElseThrow(
                () -> new NotFoundException(ErrorCode.DAILY_MEAL_NOT_FOUND)
        );

        return ReadDailyMealResponseDto.of(dailyMeal,
                null,
                null,
                null,
                null,
                dailyBookMarkedFeedRepository.findByDailyMealId(dailyMeal.getId()),
                null
        );
    }

    /**
     * 특정 일자의 반려견의 식사 내역중 즐겨찾기 포장 간식만 반환
     *
     * @param username
     * @param petId
     * @param date
     * @return
     */
    public ReadDailyMealResponseDto getDailyMealWithDailyBookMarkedPackagedSnacks(String username, Long petId, LocalDate date) {
        validUserAndFindPet(username, petId);

        LocalDateTime startDatetime = LocalDateTime.of(date.minusDays(1), LocalTime.of(0, 0, 0));
        LocalDateTime endDatetime = LocalDateTime.of(date, LocalTime.of(23, 59, 59));

        DailyMeal dailyMeal = dailyMealRepository.findByPetIdAndCreatedAtBetween(petId, startDatetime, endDatetime).orElseThrow(
                () -> new NotFoundException(ErrorCode.DAILY_MEAL_NOT_FOUND)
        );

        return ReadDailyMealResponseDto.of(dailyMeal,
                null,
                null,
                null,
                null,
                null,
                dailyBookMarkedPackagedSnackRepository.findByDailyMealId(dailyMeal.getId())
        );
    }

    /**
     * id로 조회하여 반려견의 식사 내역 반환
     *
     * @param username
     * @param petId
     * @return
     */
    public ReadDailyMealResponseDto getDailyMealWithFoods(String username, Long petId, Long dailyMealId) {
        validUserAndFindPet(username, petId);
        DailyMeal dailyMeal = dailyMealRepository.findById(dailyMealId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.DAILY_MEAL_NOT_FOUND));

        return ReadDailyMealResponseDto.of(dailyMeal,
                dailyRawRepository.findByDailyMealId(dailyMeal.getId()),
                dailyFeedRepository.findByDailyMealId(dailyMeal.getId()),
                dailyPackagedSnackRepository.findByDailyMealId(dailyMeal.getId()),
                dailyBookMarkedRawRepository.findByDailyMealId(dailyMeal.getId()),
                dailyBookMarkedFeedRepository.findByDailyMealId(dailyMeal.getId()),
                dailyBookMarkedPackagedSnackRepository.findByDailyMealId(dailyMeal.getId())
        );
    }

    /**
     * 반려견의 모든 식사 내역 반환
     *
     * @param username
     * @param petId
     * @return
     */
    public List<ReadDailyMealResponseDto> getDailyMealsWithAllFoods(String username, Long petId) {
        validUserAndFindPet(username, petId);

        List<ReadDailyMealResponseDto> response = new ArrayList<>();
        dailyMealRepository.findByPetIdOrderByCreatedAtDesc(petId).forEach(dailyMeal -> {

            response.add(ReadDailyMealResponseDto.of(dailyMeal,
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
