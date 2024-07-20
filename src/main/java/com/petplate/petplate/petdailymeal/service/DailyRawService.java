package com.petplate.petplate.petdailymeal.service;

import com.petplate.petplate.common.response.error.ErrorCode;
import com.petplate.petplate.common.response.error.exception.BadRequestException;
import com.petplate.petplate.common.response.error.exception.NotFoundException;
import com.petplate.petplate.pet.domain.entity.Pet;
import com.petplate.petplate.pet.repository.PetRepository;
import com.petplate.petplate.petdailymeal.domain.entity.DailyMeal;
import com.petplate.petplate.petdailymeal.domain.entity.DailyRaw;
import com.petplate.petplate.petdailymeal.dto.request.CreateDailyRawRequestDto;
import com.petplate.petplate.petdailymeal.dto.response.ReadDailyRawResponseDto;
import com.petplate.petplate.petdailymeal.dto.response.ReadDailyRawWithRawIdResponseDto;
import com.petplate.petplate.petdailymeal.repository.DailyMealRepository;
import com.petplate.petplate.petdailymeal.repository.DailyRawRepository;
import com.petplate.petplate.utils.DailyMealUtil;
import com.petplate.petplate.petfood.domain.entity.Raw;
import com.petplate.petplate.petfood.repository.RawRepository;
import com.petplate.petplate.utils.PetUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
     * @return DailyRaw를 생성 후 id를 반환.
     */
    @Transactional
    public Long createDailyRaw(String username, Long petId, CreateDailyRawRequestDto requestDto) {
        Pet pet = PetUtil.validUserAndFindPet(username, petId, petRepository);

        // 오늘의 dailyMeal
        DailyMeal dailyMeal = DailyMealUtil.getDailyMealToday(pet, dailyMealRepository);

        // DailyRaw 생성
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
        Pet pet = PetUtil.validUserAndFindPet(username, petId, petRepository);

        DailyMeal dailyMeal = dailyMealRepository.findById(dailyMealId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.DAILY_MEAL_NOT_FOUND));

        if (!dailyMeal.getPet().getId().equals(pet.getId())) {
            throw new BadRequestException(ErrorCode.NOT_PET_DAILY_MEAL);
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
    public List<ReadDailyRawWithRawIdResponseDto> getRecentDailyRaws(String username, Long petId, int count) {
        PetUtil.validUserAndFindPet(username, petId, petRepository);

        List<ReadDailyRawWithRawIdResponseDto> responses = new ArrayList<>();
        dailyMealRepository.findByPetIdOrderByCreatedAtDesc(petId).stream().limit(count)
                .forEach(dailyMeal -> {

                    dailyRawRepository.findByDailyMealId(dailyMeal.getId()).forEach(dailyRaw -> {
                        // dailyRaw의 Raw가 삭제되는 등의 이유로 null인 경우 반환하지 않는다.
                        if (dailyRaw.getRaw() != null) {
                            responses.add(ReadDailyRawWithRawIdResponseDto.from(dailyRaw));
                        }
                    });
                });

        return responses;
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
        Pet pet = PetUtil.validUserAndFindPet(username, petId, petRepository);

        DailyRaw dailyRaw = dailyRawRepository.findById(dailyRawId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.DAILY_RAW_NOT_FOUND));

        if (!dailyRaw.getDailyMeal().getPet().getId().equals(pet.getId())) {
            throw new BadRequestException(ErrorCode.NOT_PET_FOOD);
        }

        return ReadDailyRawResponseDto.from(dailyRaw);
    }

    /**
     * DailyRaw 제거
     *
     * @param dailyRawId
     */
    @Transactional
    public void deleteDailyRaw(String username, Long petId, Long dailyRawId) {
        Pet pet = PetUtil.validUserAndFindPet(username, petId, petRepository);
        DailyRaw dailyRaw =
                dailyRawRepository.findById(dailyRawId).orElseThrow(() -> new NotFoundException(ErrorCode.DAILY_RAW_NOT_FOUND));

        if (!dailyRaw.getDailyMeal().getPet().getId().equals(pet.getId())) {
            throw new BadRequestException(ErrorCode.NOT_PET_FOOD);
        }

        // dailyMeal에서 삭제한 dailyRaw만큼의 영양분 제거
        DailyMeal dailyMeal = dailyRaw.getDailyMeal();
        dailyMeal.subtractKcal(dailyRaw.getKcal());
        dailyMeal.subtractNutrient(dailyRaw.getNutrient());

        // 영양소 보정
        DailyMealUtil.compensatingNutrient(dailyMeal);

        dailyRawRepository.delete(dailyRaw);
    }

    /**
     * 특정 dailyMeal의 모든 dailyRaw 제거
     *
     * @param username
     * @param petId
     * @param dailyMealId
     */
    @Transactional
    public void deleteDailyRaws(String username, Long petId, Long dailyMealId) {
        Pet pet = PetUtil.validUserAndFindPet(username, petId, petRepository);

        DailyMeal dailyMeal = dailyMealRepository.findById(dailyMealId).orElseThrow(() ->
                new NotFoundException(ErrorCode.DAILY_MEAL_NOT_FOUND)
        );
        if (!dailyMeal.getPet().getId().equals(pet.getId())) {
            throw new BadRequestException(ErrorCode.NOT_PET_DAILY_MEAL);
        }

        List<DailyRaw> dailyRaws = dailyRawRepository.findByDailyMealId(dailyMealId);

        // 삭제한 만큼 dailyMeal에서 영양소 제거
        dailyRaws.forEach(dailyRaw -> {
            dailyMeal.subtractKcal(dailyRaw.getKcal());
            dailyMeal.subtractNutrient(dailyRaw.getNutrient());
        });

        // 영양소 보정
        DailyMealUtil.compensatingNutrient(dailyMeal);

        // 전체 삭제
        dailyRawRepository.deleteAll(dailyRaws);
    }
}
