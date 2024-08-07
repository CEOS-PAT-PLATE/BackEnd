package com.petplate.petplate.petdailymeal.service;

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
import com.petplate.petplate.utils.DailyMealUtil;
import com.petplate.petplate.petfood.domain.entity.BookMarkedPackagedSnack;
import com.petplate.petplate.petfood.repository.BookMarkedPackagedSnackRepository;
import com.petplate.petplate.utils.PetUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
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
        Pet pet = PetUtil.validUserAndFindPet(username, petId, petRepository);
        BookMarkedPackagedSnack bookMarkedPackagedSnack = bookMarkedPackagedSnackRepository.findById(requestDto.getBookMarkedPackagedSnackId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.BOOK_MARK_NOT_FOUND));

        DailyMeal dailyMealToday = DailyMealUtil.getDailyMealToday(pet, dailyMealRepository);

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

    public List<ReadDailyBookMarkedPackagedSnackResponseDto> getBookMarkedPackagedSnacks(String username, Long petId, Long dailyMealId) {
        Pet pet = PetUtil.validUserAndFindPet(username, petId, petRepository);

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
        PetUtil.validUserAndFindPet(username, petId, petRepository);
        DailyBookMarkedPackagedSnack dailyBookMarkedPackagedSnack = dailyBookMarkedPackagedSnackRepository.findById(dailyBookMarkedPackagedSnackId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.DAILY_BOOK_MARKED_NOT_FOUND));

        // dailyMeal에서 삭제한 dailyBookMarkedPackagedSnack만큼의 영양분 제거
        DailyMeal dailyMeal = dailyBookMarkedPackagedSnack.getDailyMeal();
        BookMarkedPackagedSnack bookMarkedPackagedSnack = dailyBookMarkedPackagedSnack.getBookMarkedPackagedSnack();
        dailyMeal.subtractKcal(bookMarkedPackagedSnack.getKcal());
        dailyMeal.subtractNutrient(bookMarkedPackagedSnack.getNutrient());

        // 영양소 보정
        DailyMealUtil.compensatingNutrient(dailyMeal);

        dailyBookMarkedPackagedSnackRepository.delete(dailyBookMarkedPackagedSnack);
    }

    /**
     * 특정 dailyMeal의 모든 dailyBookMarkedPackagedSnack 제거
     *
     * @param username
     * @param petId
     * @param dailyMealId
     */
    @Transactional
    public void deleteDailyBookMarkedPackagedSnacks(String username, Long petId, Long dailyMealId) {
        Pet pet = PetUtil.validUserAndFindPet(username, petId, petRepository);

        DailyMeal dailyMeal = dailyMealRepository.findById(dailyMealId).orElseThrow(() ->
                new NotFoundException(ErrorCode.DAILY_MEAL_NOT_FOUND)
        );
        if (!dailyMeal.getPet().getId().equals(pet.getId())) {
            throw new BadRequestException(ErrorCode.NOT_PET_DAILY_MEAL);
        }

        List<DailyBookMarkedPackagedSnack> dailyBookMarkedPackagedSnacks = dailyBookMarkedPackagedSnackRepository.findByDailyMealId(dailyMealId);

        // 삭제한 만큼 dailyMeal에서 영양소 제거
        dailyBookMarkedPackagedSnacks.forEach(dailyBookMarkedPackagedSnack -> {

            // bookMarkedPackagedSnack이 제거되지 않은 경우 (= "존재하지 않는 음식입니다"가 아닌 경우)만 dailyBookMarkedPackagedSnack을 제거 가능
            if (dailyBookMarkedPackagedSnack.getBookMarkedPackagedSnack() != null) {
                dailyMeal.subtractKcal(dailyBookMarkedPackagedSnack.getBookMarkedPackagedSnack().getKcal());
                dailyMeal.subtractNutrient(dailyBookMarkedPackagedSnack.getBookMarkedPackagedSnack().getNutrient());

                // dailyBookMarkedPackagedSnack 제거
                dailyBookMarkedPackagedSnackRepository.delete(dailyBookMarkedPackagedSnack);
            } else {
                log.info("원인: dailyBookMarkedPackagedSnack.getBookMarkedPackagedSnack() 메서드가 null을 반환함\n" +
                        "결과: 해당 dailyBookMarkedPackagedSnack을 제거할 수 없음");
            }
        });

        // 영양소 보정
        DailyMealUtil.compensatingNutrient(dailyMeal);
    }
}
