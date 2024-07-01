package com.petplate.petplate.petdailymeal.service;

import com.petplate.petplate.common.EmbeddedType.Nutrient;
import com.petplate.petplate.common.EmbeddedType.Vitamin;
import com.petplate.petplate.common.response.error.ErrorCode;
import com.petplate.petplate.common.response.error.exception.BadRequestException;
import com.petplate.petplate.common.response.error.exception.NotFoundException;
import com.petplate.petplate.pet.domain.entity.Pet;
import com.petplate.petplate.pet.repository.PetRepository;
import com.petplate.petplate.petdailymeal.domain.entity.DailyMeal;
import com.petplate.petplate.petdailymeal.domain.entity.RawDailyMeal;
import com.petplate.petplate.petdailymeal.dto.request.AddRawDailyMealRequestDto;
import com.petplate.petplate.petdailymeal.dto.response.ReadRawDailyMealResponseDto;
import com.petplate.petplate.petdailymeal.repository.DailyMealRepository;
import com.petplate.petplate.petdailymeal.repository.RawDailyMealRepository;
import com.petplate.petplate.petfood.domain.entity.Raw;
import com.petplate.petplate.petfood.repository.RawRepository;
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
public class RawDailyMealService {
    private final RawRepository rawRepository;
    private final DailyMealRepository dailyMealRepository;
    private final RawDailyMealRepository rawDailyMealRepository;
    private final PetRepository petRepository;

    @Transactional
    public Long createRawDailyMeal(String username, Long petId, AddRawDailyMealRequestDto requestDto) {
        Pet pet = findPet(username, petId);

        // 오늘의 dailyMeal
        LocalDateTime startDatetime = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(0, 0, 0));
        LocalDateTime endDatetime = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59));
        DailyMeal dailyMeal = getDailyMeal(petId, startDatetime, endDatetime, pet);

        // rawDailyMeal 생성
        Raw raw = rawRepository.findById(requestDto.getRawId()).orElseThrow(
                () -> new BadRequestException(ErrorCode.RAW_NOT_FOUND));
        RawDailyMeal rawDailyMeal = RawDailyMeal.builder()
                .dailyMeal(dailyMeal).raw(raw).serving(requestDto.getServing())
                .build();
        rawDailyMealRepository.save(rawDailyMeal);

        // dailyMeal에 먹은만큼 칼로리, 영양소 추가
        dailyMeal.addKcal(rawDailyMeal.getKcal());
        dailyMeal.addNutrient(rawDailyMeal.getNutrient());

        return rawDailyMeal.getId();
    }

    public List<ReadRawDailyMealResponseDto> getRawDailyMeals(String username, Long petId, Long dailyMealId) {
        Pet pet = findPet(username, petId);

        DailyMeal dailyMeal = dailyMealRepository.findById(dailyMealId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.DAILY_MEAL_NOT_FOUND));

        if(!Objects.equals(dailyMeal.getPet().getId(), pet.getId())) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        List<ReadRawDailyMealResponseDto> responses = new ArrayList<>();
        rawDailyMealRepository.findByDailyMealId(dailyMealId).forEach(rawDailyMeal -> {
            responses.add(ReadRawDailyMealResponseDto.from(rawDailyMeal));
        });

        return responses;
    }

    public ReadRawDailyMealResponseDto getRawDailyMeal(String username, Long petId, Long rawDailyMealId) {
        Pet pet = findPet(username, petId);

        RawDailyMeal rawDailyMeal = rawDailyMealRepository.findById(rawDailyMealId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.RAW_DAILY_MEAL_NOT_FOUND));

        if(!Objects.equals(rawDailyMeal.getDailyMeal().getPet().getId(), pet.getId())) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        return ReadRawDailyMealResponseDto.from(rawDailyMeal);
    }

    @Transactional
    public void deleteRawDailyMeal(Long rawDailyMealId) {
        RawDailyMeal rawDailyMeal =
                rawDailyMealRepository.findById(rawDailyMealId).orElseThrow(() -> new NotFoundException(ErrorCode.RAW_DAILY_MEAL_NOT_FOUND));

        // dailyMeal에서 삭제한 rawDailyMeal만큼의 영양분 제거
        DailyMeal dailyMeal = rawDailyMeal.getDailyMeal();
        dailyMeal.subtractKcal(rawDailyMeal.getKcal());
        dailyMeal.subtractNutrient(rawDailyMeal.getNutrient());

        rawDailyMealRepository.delete(rawDailyMeal);
    }

    private DailyMeal getDailyMeal(Long petId, LocalDateTime startDatetime, LocalDateTime endDatetime, Pet pet) {
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

    private Pet findPet(String username, Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));

        // 조회하려는 반려견이 본인의 반려견이 아닌 경우 예외 발생
        if (!pet.getOwner().getUsername().equals(username)) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        return pet;
    }
}
