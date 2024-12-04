package com.petplate.petplate.petdailymeal.service;

import com.petplate.petplate.common.response.error.ErrorCode;
import com.petplate.petplate.common.response.error.exception.BadRequestException;
import com.petplate.petplate.common.response.error.exception.NotFoundException;
import com.petplate.petplate.pet.domain.entity.Pet;
import com.petplate.petplate.pet.repository.PetRepository;
import com.petplate.petplate.petdailymeal.domain.entity.DailyBookMarkedFeed;
import com.petplate.petplate.petdailymeal.domain.entity.DailyMeal;
import com.petplate.petplate.petdailymeal.dto.request.CreateDailyBookMarkedFeedRequestDto;
import com.petplate.petplate.petdailymeal.dto.response.ReadDailyBookMarkedFeedResponseDto;
import com.petplate.petplate.petdailymeal.repository.DailyBookMarkedFeedRepository;
import com.petplate.petplate.petdailymeal.repository.DailyMealRepository;
import com.petplate.petplate.utils.DailyMealUtil;
import com.petplate.petplate.petfood.domain.entity.BookMarkedFeed;
import com.petplate.petplate.petfood.repository.BookMarkedFeedRepository;
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
public class DailyBookMarkedFeedService {
    private final BookMarkedFeedRepository bookMarkedFeedRepository;
    private final DailyBookMarkedFeedRepository dailyBookMarkedFeedRepository;
    private final PetRepository petRepository;
    private final DailyMealRepository dailyMealRepository;

    /**
     * 오늘 식사에 즐겨찾기 사료를 추가함
     *
     * @param username
     * @param petId
     * @param requestDto
     * @return
     */
    @Transactional
    public Long createDailyBookMarkedFeed(String username, Long petId, CreateDailyBookMarkedFeedRequestDto requestDto) {
        Pet pet = PetUtil.validUserAndFindPet(username, petId, petRepository);
        BookMarkedFeed bookMarkedFeed = bookMarkedFeedRepository.findById(requestDto.getBookMarkedFeedId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.BOOK_MARK_NOT_FOUND));

        DailyMeal dailyMealToday = DailyMealUtil.getDailyMealToday(pet,dailyMealRepository);

        DailyBookMarkedFeed dailyBookMarkedFeed = DailyBookMarkedFeed.builder()
                .bookMarkedFeed(bookMarkedFeed)
                .dailyMeal(dailyMealToday)
                .build();

        dailyBookMarkedFeedRepository.save(dailyBookMarkedFeed);

        // dailyMeal에 먹은만큼 칼로리, 영양소 추가
        dailyMealToday.addKcal(dailyBookMarkedFeed.getBookMarkedFeed().getKcal());
        dailyMealToday.addNutrient(dailyBookMarkedFeed.getBookMarkedFeed().getNutrient());

        return dailyBookMarkedFeed.getId();
    }

    public List<ReadDailyBookMarkedFeedResponseDto> getBookMarkedFeeds(String username, Long petId, Long dailyMealId) {
        Pet pet = PetUtil.validUserAndFindPet(username, petId, petRepository);

        DailyMeal dailyMeal = dailyMealRepository.findById(dailyMealId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.DAILY_MEAL_NOT_FOUND));

        if (!Objects.equals(dailyMeal.getPet().getId(), pet.getId())) {
            throw new BadRequestException(ErrorCode.NOT_PET_DAILY_MEAL);
        }

        List<ReadDailyBookMarkedFeedResponseDto> responses = new ArrayList<>();
        dailyBookMarkedFeedRepository.findByDailyMealId(dailyMeal.getId()).forEach(
                dailyBookMarkedFeed -> responses.add(ReadDailyBookMarkedFeedResponseDto.from(dailyBookMarkedFeed)));

        return responses;
    }

    @Transactional
    public void deleteDailyBookMarkedFeed(String username, Long petId, Long dailyBookMarkedFeedId) {
        PetUtil.validUserAndFindPet(username, petId, petRepository);

        DailyBookMarkedFeed dailyBookMarkedFeed = dailyBookMarkedFeedRepository.findById(dailyBookMarkedFeedId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.DAILY_BOOK_MARKED_NOT_FOUND));

        // dailyMeal에서 삭제한 dailyBookMarkedFeed만큼의 영양분 제거
        DailyMeal dailyMeal = dailyBookMarkedFeed.getDailyMeal();
        BookMarkedFeed bookMarkedFeed = dailyBookMarkedFeed.getBookMarkedFeed();
        dailyMeal.subtractKcal(bookMarkedFeed.getKcal());
        dailyMeal.subtractNutrient(bookMarkedFeed.getNutrient());

        // 영양소 보정
        DailyMealUtil.compensatingNutrient(dailyMeal);

        dailyBookMarkedFeedRepository.delete(dailyBookMarkedFeed);
    }

    /**
     * 특정 dailyMeal의 모든 dailyBookMarkedFeed 제거
     *
     * @param username
     * @param petId
     * @param dailyMealId
     */
    @Transactional
    public void deleteDailyBookMarkedFeeds(String username, Long petId, Long dailyMealId) {
        Pet pet = PetUtil.validUserAndFindPet(username, petId, petRepository);

        DailyMeal dailyMeal = dailyMealRepository.findById(dailyMealId).orElseThrow(() ->
                new NotFoundException(ErrorCode.DAILY_MEAL_NOT_FOUND)
        );
        if (!dailyMeal.getPet().getId().equals(pet.getId())) {
            throw new BadRequestException(ErrorCode.NOT_PET_DAILY_MEAL);
        }

        List<DailyBookMarkedFeed> dailyBookMarkedFeeds = dailyBookMarkedFeedRepository.findByDailyMealId(dailyMealId);

        // 삭제한 만큼 dailyMeal에서 영양소 제거
        dailyBookMarkedFeeds.forEach(dailyBookMarkedFeed -> {

            // bookMarkedFeed가 제거되지 않은 경우 (= "존재하지 않는 음식입니다"가 아닌 경우)만 dailyBookMarkedFeed를 제거 가능
            if (dailyBookMarkedFeed.getBookMarkedFeed() != null) {
                dailyMeal.subtractKcal(dailyBookMarkedFeed.getBookMarkedFeed().getKcal());
                dailyMeal.subtractNutrient(dailyBookMarkedFeed.getBookMarkedFeed().getNutrient());

                // dailyBookMarkedFeed 제거
                dailyBookMarkedFeedRepository.delete(dailyBookMarkedFeed);
            } else {
                log.info("원인: dailyBookMarkedFeed.getBookMarkedFeed() 메서드가 null을 반환함\n" +
                        "결과: 해당 dailyBookMarkedFeed를 제거할 수 없음");
            }
        });

        // 영양소 보정
        DailyMealUtil.compensatingNutrient(dailyMeal);
    }
}
