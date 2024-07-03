package com.petplate.petplate.petdailymeal.service;

import com.petplate.petplate.common.EmbeddedType.Nutrient;
import com.petplate.petplate.common.EmbeddedType.Vitamin;
import com.petplate.petplate.common.response.error.ErrorCode;
import com.petplate.petplate.common.response.error.exception.BadRequestException;
import com.petplate.petplate.common.response.error.exception.NotFoundException;
import com.petplate.petplate.pet.domain.entity.Pet;
import com.petplate.petplate.pet.repository.PetRepository;
import com.petplate.petplate.petdailymeal.domain.entity.DailyMeal;
import com.petplate.petplate.petdailymeal.domain.entity.DailyRaw;
import com.petplate.petplate.petdailymeal.dto.request.CreateDailyRawRequestDto;
import com.petplate.petplate.petdailymeal.dto.response.ReadDailyRawResponseDto;
import com.petplate.petplate.petdailymeal.repository.DailyMealRepository;
import com.petplate.petplate.petdailymeal.repository.DailyRawRepository;
import com.petplate.petplate.petfood.domain.entity.Raw;
import com.petplate.petplate.petfood.repository.RawRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class DailyRawService {
    private final RawRepository rawRepository;
    private final DailyMealRepository dailyMealRepository;
    private final DailyRawRepository dailyRawRepository;
    private final PetRepository petRepository;

    /**
     * RawDailyMeal 생성
     *
     * @param username
     * @param petId
     * @param requestDto
     * @return rawDailyMeal를 생성 후 id를 반환.
     */
    @Transactional
    public Long createDailyRaw(String username, Long petId, CreateDailyRawRequestDto requestDto) {
        Pet pet = findPet(username, petId);

        // 오늘의 dailyMeal
        LocalDateTime startDatetime = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(0, 0, 0));
        LocalDateTime endDatetime = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59));
        DailyMeal dailyMeal = getDailyMeal(petId, startDatetime, endDatetime, pet);

        // rawDailyMeal 생성
        Raw raw = rawRepository.findById(requestDto.getRawId()).orElseThrow(
                () -> new NotFoundException(ErrorCode.RAW_NOT_FOUND));

        DailyRaw dailyRaw = DailyRaw.builder()
                .dailyMeal(dailyMeal).raw(raw).serving(requestDto.getServing())
                .build();

        dailyRawRepository.save(dailyRaw);

        // dailyMeal에 먹은만큼 칼로리, 영양소 추가
        dailyMeal.addKcal(dailyRaw.getKcal());
        dailyMeal.addNutrient(dailyRaw.getNutrient());

        return dailyRaw.getId();
    }

    /**
     * 특정 DailyMeal에 대한 모든 DailyRaw 조회
     *
     * @param username
     * @param petId
     * @param dailyMealId
     * @return
     */
    public List<ReadDailyRawResponseDto> getDailyRaws(String username, Long petId, Long dailyMealId) {
        Pet pet = findPet(username, petId);

        DailyMeal dailyMeal = dailyMealRepository.findById(dailyMealId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.DAILY_MEAL_NOT_FOUND));

        if (!Objects.equals(dailyMeal.getPet().getId(), pet.getId())) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        List<ReadDailyRawResponseDto> responses = new ArrayList<>();
        dailyRawRepository.findByDailyMealId(dailyMealId).forEach(dailyRaw -> {
            responses.add(ReadDailyRawResponseDto.from(dailyRaw));
        });

        return responses;
    }

    /**
     * 최근 count 번의 식사에서 먹었던 DailyRaw의 정보를 반환
     *
     * @param username
     * @param petId
     * @param count
     * @return
     */
    public List<ReadDailyRawResponseDto> getRecentDailyRaws(String username, Long petId, int count) {
        findPet(username, petId);

        return dailyMealRepository.findByPetIdOrderByCreatedAtDesc(petId).stream()
                .limit(count)
                .flatMap(dailyMeal -> dailyRawRepository.findByDailyMealId(dailyMeal.getId()).stream())
                .map(ReadDailyRawResponseDto::from)
                .collect(Collectors.toList());
    }

    /**
     * pk로 DailyRaw 조회
     *
     * @param username
     * @param petId
     * @param dailyRawId
     * @return
     */
    public ReadDailyRawResponseDto getDailyRaw(String username, Long petId, Long dailyRawId) {
        Pet pet = findPet(username, petId);

        DailyRaw dailyRaw = dailyRawRepository.findById(dailyRawId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.RAW_DAILY_MEAL_NOT_FOUND));

        if (!Objects.equals(dailyRaw.getDailyMeal().getPet().getId(), pet.getId())) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        return ReadDailyRawResponseDto.from(dailyRaw);
    }

    /**
     * rawDailyMeal 제거
     *
     * @param dailyRawId
     */
    @Transactional
    public void deleteDailyRaw(String username, Long petId, Long dailyRawId) {
        findPet(username, petId);
        DailyRaw dailyRaw =
                dailyRawRepository.findById(dailyRawId).orElseThrow(() -> new NotFoundException(ErrorCode.RAW_DAILY_MEAL_NOT_FOUND));

        // dailyMeal에서 삭제한 rawDailyMeal만큼의 영양분 제거
        DailyMeal dailyMeal = dailyRaw.getDailyMeal();
        dailyMeal.subtractKcal(dailyRaw.getKcal());
        dailyMeal.subtractNutrient(dailyRaw.getNutrient());

        dailyRawRepository.delete(dailyRaw);
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
                .orElseThrow(() -> new NotFoundException(ErrorCode.PET_NOT_FOUND));

        // 조회하려는 반려견이 본인의 반려견이 아닌 경우 예외 발생
        if (!pet.getOwner().getUsername().equals(username)) {
            throw new BadRequestException(ErrorCode.NOT_USER_PET);
        }

        return pet;
    }
}
