package com.petplate.petplate.petdailymeal.service;

import com.petplate.petplate.common.EmbeddedType.Nutrient;
import com.petplate.petplate.common.EmbeddedType.Vitamin;
import com.petplate.petplate.common.response.error.ErrorCode;
import com.petplate.petplate.common.response.error.exception.BadRequestException;
import com.petplate.petplate.common.response.error.exception.NotFoundException;
import com.petplate.petplate.pet.domain.entity.Pet;
import com.petplate.petplate.pet.repository.PetRepository;
import com.petplate.petplate.petdailymeal.domain.entity.DailyBookMarkedPackagedSnack;
import com.petplate.petplate.petdailymeal.domain.entity.DailyMeal;
import com.petplate.petplate.petdailymeal.dto.request.CreateDailyBookMarkedPackagedSnackRequestDto;
import com.petplate.petplate.petdailymeal.dto.response.ReadDailyBookMarkedPackagedSnackResponseDto;
import com.petplate.petplate.petdailymeal.repository.DailyBookMarkedPackagedSnackRepository;
import com.petplate.petplate.petdailymeal.repository.DailyMealRepository;
import com.petplate.petplate.petfood.domain.entity.BookMarkedPackagedSnack;
import com.petplate.petplate.petfood.repository.BookMarkedPackagedSnackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DailyBookMarkedPackagedSnackService {
    private final BookMarkedPackagedSnackRepository bookMarkedPackagedSnackRepository;
    private final DailyBookMarkedPackagedSnackRepository dailyBookMarkedPackagedSnackRepository;
    private final PetRepository petRepository;
    private final DailyMealRepository dailyMealRepository;

    /**
     * 오늘 식사에 즐겨찾기 포장간식을 추가함
     *
     * @param username
     * @param petId
     * @param requestDto
     * @return
     */
    @Transactional
    public Long createDailyBookMarkedPackagedSnack(String username, Long petId, CreateDailyBookMarkedPackagedSnackRequestDto requestDto) {
        Pet pet = validUserAndFindPet(username, petId);
        BookMarkedPackagedSnack bookMarkedPackagedSnack = bookMarkedPackagedSnackRepository.findById(requestDto.getBookMarkedPackagedSnackId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.BOOK_MARK_NOT_FOUND));

        DailyMeal dailyMealToday = getDailyMealToday(pet);

        DailyBookMarkedPackagedSnack dailyBookMarkedPackagedSnack = DailyBookMarkedPackagedSnack.builder()
                .bookMarkedPackagedSnack(bookMarkedPackagedSnack)
                .dailyMeal(dailyMealToday)
                .build();

        dailyBookMarkedPackagedSnackRepository.save(dailyBookMarkedPackagedSnack);

        // dailyMeal에 먹은만큼 칼로리, 영양소 추가
        dailyMealToday.addKcal(dailyBookMarkedPackagedSnack.getBookMarkedPackagedSnack().getKcal());
        dailyMealToday.addNutrient(dailyBookMarkedPackagedSnack.getBookMarkedPackagedSnack().getNutrient());

        return dailyBookMarkedPackagedSnack.getId();
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

    public List<ReadDailyBookMarkedPackagedSnackResponseDto> getBookMarkedPackagedSnacks(String username, Long petId, Long dailyMealId) {
        Pet pet = validUserAndFindPet(username, petId);

        DailyMeal dailyMeal = dailyMealRepository.findById(dailyMealId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.DAILY_MEAL_NOT_FOUND));

        if (!Objects.equals(dailyMeal.getPet().getId(), pet.getId())) {
            throw new BadRequestException(ErrorCode.NOT_PET_DAILY_MEAL);
        }

        List<ReadDailyBookMarkedPackagedSnackResponseDto> responses = new ArrayList<>();
        dailyBookMarkedPackagedSnackRepository.findByDailyMealId(dailyMeal.getId()).forEach(
                dailyBookMarkedPackagedSnack -> responses.add(ReadDailyBookMarkedPackagedSnackResponseDto.from(dailyBookMarkedPackagedSnack)));

        return responses;
    }

    @Transactional
    public void deleteDailyBookMarkedPackagedSnack(String username, Long petId, Long dailyBookMarkedPackagedSnackId) {
        validUserAndFindPet(username, petId);
        DailyBookMarkedPackagedSnack dailyBookMarkedPackagedSnack = dailyBookMarkedPackagedSnackRepository.findById(dailyBookMarkedPackagedSnackId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.DAILY_BOOK_MARKED_NOT_FOUND));

        // dailyMeal에서 삭제한 dailyBookMarkedPackagedSnack만큼의 영양분 제거
        DailyMeal dailyMeal = dailyBookMarkedPackagedSnack.getDailyMeal();
        BookMarkedPackagedSnack bookMarkedPackagedSnack = dailyBookMarkedPackagedSnack.getBookMarkedPackagedSnack();
        dailyMeal.subtractKcal(bookMarkedPackagedSnack.getKcal());
        dailyMeal.subtractNutrient(bookMarkedPackagedSnack.getNutrient());

        dailyBookMarkedPackagedSnackRepository.delete(dailyBookMarkedPackagedSnack);
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
