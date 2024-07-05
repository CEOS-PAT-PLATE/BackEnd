package com.petplate.petplate.petdailymeal.service;

import com.petplate.petplate.common.EmbeddedType.Nutrient;
import com.petplate.petplate.common.EmbeddedType.Vitamin;
import com.petplate.petplate.common.response.error.ErrorCode;
import com.petplate.petplate.common.response.error.exception.BadRequestException;
import com.petplate.petplate.common.response.error.exception.NotFoundException;
import com.petplate.petplate.pet.domain.entity.Pet;
import com.petplate.petplate.pet.repository.PetRepository;
import com.petplate.petplate.petdailymeal.domain.entity.DailyBookMarkedRaw;
import com.petplate.petplate.petdailymeal.domain.entity.DailyMeal;
import com.petplate.petplate.petdailymeal.dto.request.CreateDailyBookMarkedRawRequestDto;
import com.petplate.petplate.petdailymeal.dto.response.ReadDailyBookMarkedRawResponseDto;
import com.petplate.petplate.petdailymeal.dto.response.ReadDailyRawResponseDto;
import com.petplate.petplate.petdailymeal.repository.DailyBookMarkedRawRepository;
import com.petplate.petplate.petdailymeal.repository.DailyMealRepository;
import com.petplate.petplate.petfood.domain.entity.BookMarkedRaw;
import com.petplate.petplate.petfood.dto.response.ReadBookMarkedRawResponseDto;
import com.petplate.petplate.petfood.repository.BookMarkedRawRepository;
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
public class DailyBookMarkedRawService {
    private final DailyMealRepository dailyMealRepository;
    private final DailyBookMarkedRawRepository dailyBookMarkedRawRepository;
    private final BookMarkedRawRepository bookMarkedRawRepository;
    private final PetRepository petRepository;

    @Transactional
    public Long createDailyBookMarkedRaw(String username, Long petId, CreateDailyBookMarkedRawRequestDto requestDto) {
        Pet pet = validUserAndFindPet(username, petId);
        BookMarkedRaw bookMarkedRaw = bookMarkedRawRepository.findById(requestDto.getBookMarkedRawId()).orElseThrow(() ->
                new NotFoundException(ErrorCode.BOOK_MARK_NOT_FOUND)
        );

        DailyMeal dailyMealToday = getDailyMealToday(pet);

        DailyBookMarkedRaw dailyBookMarkedRaw = DailyBookMarkedRaw.builder()
                .bookMarkedRaw(bookMarkedRaw)
                .dailyMeal(dailyMealToday)
                .build();

        dailyBookMarkedRawRepository.save(dailyBookMarkedRaw);

        return dailyBookMarkedRaw.getId();
    }

    public List<ReadDailyBookMarkedRawResponseDto> getBookMarkedRaws(String username, Long petId, LocalDate date) {
        Pet pet = validUserAndFindPet(username, petId);

        DailyMeal dailyMeal = findDailyMeal(petId, date);

        if (!Objects.equals(dailyMeal.getPet().getId(), pet.getId())) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        List<ReadDailyBookMarkedRawResponseDto> responses = new ArrayList<>();
        dailyBookMarkedRawRepository.findByDailyMealId(dailyMeal.getId()).forEach(dailyBookMarkedRaw -> {
                    responses.add(ReadDailyBookMarkedRawResponseDto.from(dailyBookMarkedRaw));
                }
        );

        return responses;
    }

    public List<ReadBookMarkedRawResponseDto> getRecentBookMarkedRaws(String username, Long petId, int count) {
        validUserAndFindPet(username, petId);

        List<ReadBookMarkedRawResponseDto> responses = new ArrayList<>();
        dailyMealRepository.findByPetIdOrderByCreatedAtDesc(petId).stream()
                .limit(count)
                .flatMap(dailyMeal -> dailyBookMarkedRawRepository.findByDailyMealId(dailyMeal.getId()).stream())
                .forEach(dailyBookMarkedRaw -> {
                    BookMarkedRaw bookMarkedRaw = dailyBookMarkedRaw.getBookMarkedRaw();
                    responses.add(ReadBookMarkedRawResponseDto.from(bookMarkedRaw));
                });

        return responses;
    }

    @Transactional
    public void deleteDailyBookMarkedRaw(String username, Long petId, Long dailyBookMarkedRawId) {
        validUserAndFindPet(username, petId);
        DailyBookMarkedRaw dailyBookMarkedRaw = dailyBookMarkedRawRepository.findById(dailyBookMarkedRawId).orElseThrow(() -> new NotFoundException(ErrorCode.DAILY_BOOK_MARKED_NOT_FOUND));

        // dailyMeal에서 삭제한 dailyBookMarkedRaw만큼의 영양분 제거
        DailyMeal dailyMeal = dailyBookMarkedRaw.getDailyMeal();
        BookMarkedRaw bookMarkedRaw = dailyBookMarkedRaw.getBookMarkedRaw();
        dailyMeal.subtractKcal(bookMarkedRaw.getKcal());
        dailyMeal.subtractNutrient(bookMarkedRaw.getNutrient());

        dailyBookMarkedRawRepository.delete(dailyBookMarkedRaw);
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
