package com.petplate.petplate.petdailymeal.service;

import com.petplate.petplate.common.response.error.ErrorCode;
import com.petplate.petplate.common.response.error.exception.BadRequestException;
import com.petplate.petplate.common.response.error.exception.NotFoundException;
import com.petplate.petplate.pet.domain.entity.Pet;
import com.petplate.petplate.pet.repository.PetRepository;
import com.petplate.petplate.petdailymeal.domain.entity.DailyBookMarkedRaw;
import com.petplate.petplate.petdailymeal.domain.entity.DailyMeal;
import com.petplate.petplate.petdailymeal.dto.request.CreateDailyBookMarkedRawRequestDto;
import com.petplate.petplate.petdailymeal.dto.response.ReadDailyBookMarkedRawResponseDto;
import com.petplate.petplate.petdailymeal.repository.DailyBookMarkedRawRepository;
import com.petplate.petplate.petdailymeal.repository.DailyMealRepository;
import com.petplate.petplate.utils.DailyMealUtil;
import com.petplate.petplate.petfood.domain.entity.BookMarkedRaw;
import com.petplate.petplate.petfood.dto.response.ReadBookMarkedRawResponseDto;
import com.petplate.petplate.petfood.repository.BookMarkedRawRepository;
import com.petplate.petplate.utils.PetUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 오늘 식사에 즐겨찾기 자연식을 추가함
     *
     * @param username
     * @param petId
     * @param requestDto
     * @return
     */
    @Transactional
    public Long createDailyBookMarkedRaw(String username, Long petId, CreateDailyBookMarkedRawRequestDto requestDto) {
        Pet pet = PetUtil.validUserAndFindPet(username, petId, petRepository);
        BookMarkedRaw bookMarkedRaw = bookMarkedRawRepository.findById(requestDto.getBookMarkedRawId()).orElseThrow(() ->
                new NotFoundException(ErrorCode.BOOK_MARK_NOT_FOUND)
        );

        DailyMeal dailyMealToday = DailyMealUtil.getDailyMealToday(pet,dailyMealRepository);

        DailyBookMarkedRaw dailyBookMarkedRaw = DailyBookMarkedRaw.builder()
                .bookMarkedRaw(bookMarkedRaw)
                .dailyMeal(dailyMealToday)
                .build();

        dailyBookMarkedRawRepository.save(dailyBookMarkedRaw);

        // dailyMeal에 먹은만큼 칼로리, 영양소 추가
        dailyMealToday.addKcal(dailyBookMarkedRaw.getBookMarkedRaw().getKcal());
        dailyMealToday.addNutrient(dailyBookMarkedRaw.getBookMarkedRaw().getNutrient());

        return dailyBookMarkedRaw.getId();
    }

    public List<ReadDailyBookMarkedRawResponseDto> getBookMarkedRaws(String username, Long petId, Long dailyMealId) {
        Pet pet = PetUtil.validUserAndFindPet(username, petId, petRepository);

        DailyMeal dailyMeal = dailyMealRepository.findById(dailyMealId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.DAILY_MEAL_NOT_FOUND));

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
        PetUtil.validUserAndFindPet(username, petId, petRepository);

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
        PetUtil.validUserAndFindPet(username, petId, petRepository);
        DailyBookMarkedRaw dailyBookMarkedRaw = dailyBookMarkedRawRepository.findById(dailyBookMarkedRawId).orElseThrow(() -> new NotFoundException(ErrorCode.DAILY_BOOK_MARKED_NOT_FOUND));

        // dailyMeal에서 삭제한 dailyBookMarkedRaw만큼의 영양분 제거
        DailyMeal dailyMeal = dailyBookMarkedRaw.getDailyMeal();
        BookMarkedRaw bookMarkedRaw = dailyBookMarkedRaw.getBookMarkedRaw();
        dailyMeal.subtractKcal(bookMarkedRaw.getKcal());
        dailyMeal.subtractNutrient(bookMarkedRaw.getNutrient());

        // 영양소 보정
        DailyMealUtil.compensatingNutrient(dailyMeal);

        dailyBookMarkedRawRepository.delete(dailyBookMarkedRaw);
    }

    /**
     * 특정 dailyMeal의 모든 dailyBookMarkedRaw 제거
     *
     * @param username
     * @param petId
     * @param dailyMealId
     */
    @Transactional
    public void deleteDailyBookMarkedRaws(String username, Long petId, Long dailyMealId) {
        Pet pet = PetUtil.validUserAndFindPet(username, petId, petRepository);

        DailyMeal dailyMeal = dailyMealRepository.findById(dailyMealId).orElseThrow(() ->
                new NotFoundException(ErrorCode.DAILY_MEAL_NOT_FOUND)
        );
        if (!dailyMeal.getPet().getId().equals(pet.getId())) {
            throw new BadRequestException(ErrorCode.NOT_PET_DAILY_MEAL);
        }

        List<DailyBookMarkedRaw> dailyBookMarkedRaws = dailyBookMarkedRawRepository.findByDailyMealId(dailyMealId);

        // 삭제한 만큼 dailyMeal에서 영양소 제거
        dailyBookMarkedRaws.forEach(dailyBookMarkedRaw -> {
            dailyMeal.subtractKcal(dailyBookMarkedRaw.getBookMarkedRaw().getKcal());
            dailyMeal.subtractNutrient(dailyBookMarkedRaw.getBookMarkedRaw().getNutrient());
        });

        // 영양소 보정
        DailyMealUtil.compensatingNutrient(dailyMeal);

        // 전체 삭제
        dailyBookMarkedRawRepository.deleteAll(dailyBookMarkedRaws);
    }
}
