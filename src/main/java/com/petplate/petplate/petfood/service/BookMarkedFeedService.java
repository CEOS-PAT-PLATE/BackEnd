package com.petplate.petplate.petfood.service;

import com.petplate.petplate.common.EmbeddedType.Nutrient;
import com.petplate.petplate.common.EmbeddedType.Vitamin;
import com.petplate.petplate.common.response.error.ErrorCode;
import com.petplate.petplate.common.response.error.exception.BadRequestException;
import com.petplate.petplate.common.response.error.exception.NotFoundException;
import com.petplate.petplate.pet.repository.PetRepository;
import com.petplate.petplate.petdailymeal.dto.response.ReadDailyBookMarkedFeedResponseDto;
import com.petplate.petplate.petdailymeal.repository.DailyBookMarkedFeedRepository;
import com.petplate.petplate.petfood.domain.entity.BookMarkedFeed;
import com.petplate.petplate.petfood.dto.request.CreateBookMarkedFeedRequestDto;
import com.petplate.petplate.petfood.dto.response.ReadBookMarkedFeedResponseDto;
import com.petplate.petplate.petfood.repository.BookMarkedFeedRepository;
import com.petplate.petplate.user.domain.entity.User;
import com.petplate.petplate.user.repository.UserRepository;
import com.petplate.petplate.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookMarkedFeedService {
    private final BookMarkedFeedRepository bookMarkedFeedRepository;
    private final UserRepository userRepository;
    private final DailyBookMarkedFeedRepository dailyBookMarkedFeedRepository;

    // 1 IU retinol = 0.3 mcg RAE
    static final double vitaminAIuRetinolPerGram = 3333333.3333333335; // == 10E6 / 0.3

    // 1 IU = 0.025 μg
    static final double vitaminDIuPerGram = 4.0E7; // == 10E6 / 0.025

    // 1 IU (natural) = 0.67 mg Vitamin E (as alpha-tocopherol)
    // 1 IU (synthetic) = 0.45 mg Vitamin E (as alpha-tocopherol)
    static final double vitaminEIuNaturalPerGram = 1492.5373134328358; // == 10E3 / 0.67

    @Transactional
    public Long createBookMarkedFeed(String username, CreateBookMarkedFeedRequestDto requestDto) {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new NotFoundException(ErrorCode.USER_NOT_FOUND)
        );

        // 중복되는 이름과 섭취량의 즐겨찾기 사료 존재시 예외 발생
        if (bookMarkedFeedRepository.existsByUserUsernameAndNameAndServing(username, requestDto.getName(), requestDto.getServing())) {
            throw new BadRequestException(ErrorCode.BOOK_MARK_ALREADY_EXISTS);
        }

        double serving = requestDto.getServing();

        BookMarkedFeed bookMarkedFeed = BookMarkedFeed.builder()
                .user(user)
                .name(requestDto.getName())
                .kcal(requestDto.getKcal())
                .serving(requestDto.getServing())
                .nutrient(Nutrient.builder()
                        .carbonHydrate(calculateNutritionAmount(serving, requestDto.getCarbonHydratePercent()))
                        .protein(calculateNutritionAmount(serving, requestDto.getProteinPercent()))
                        .fat(calculateNutritionAmount(serving, requestDto.getFatPercent()))
                        .calcium(calculateNutritionAmount(serving, requestDto.getCalciumPercent()))
                        .phosphorus(calculateNutritionAmount(serving, requestDto.getPhosphorusPercent()))
                        .vitamin(Vitamin.builder()
                                .vitaminA(vitaminAIuRetinolPerGram * calculateNutritionAmount(serving, requestDto.getVitaminAPercent()))
                                .vitaminD(vitaminDIuPerGram * calculateNutritionAmount(serving, requestDto.getVitaminDPercent()))
                                .vitaminE(vitaminEIuNaturalPerGram * calculateNutritionAmount(serving, requestDto.getVitaminEPercent()))
                                .build())
                        .build())
                .build();

        bookMarkedFeedRepository.save(bookMarkedFeed);

        return bookMarkedFeed.getId();
    }

    private double calculateNutritionAmount(double serving, double nutritionPercent) {
        return serving * (nutritionPercent / 100);
    }

    public List<ReadBookMarkedFeedResponseDto> getBookMarkedFeeds(String username) {
        List<ReadBookMarkedFeedResponseDto> responses = new ArrayList<>();
        bookMarkedFeedRepository.findByUserUsername(username).forEach(bookMarkedFeed ->
                responses.add(ReadBookMarkedFeedResponseDto.from(bookMarkedFeed))
        );

        return responses;
    }

    public ReadBookMarkedFeedResponseDto getBookMarkedFeed(String username, Long bookMarkedFeedId) {
        if (!bookMarkedFeedRepository.existsById(bookMarkedFeedId)) {
            throw new NotFoundException(ErrorCode.BOOK_MARK_NOT_FOUND);
        }

        BookMarkedFeed bookMarkedFeed = bookMarkedFeedRepository.findByUserUsernameAndId(username, bookMarkedFeedId).orElseThrow(() ->
                new BadRequestException(ErrorCode.NOT_USER_BOOK_MARK));

        return ReadBookMarkedFeedResponseDto.from(bookMarkedFeed);
    }

    @Transactional
    public void deleteBookMarkedFeed(String username, Long bookMarkedFeedId) {
        if (!bookMarkedFeedRepository.existsById(bookMarkedFeedId)) {
            throw new NotFoundException(ErrorCode.BOOK_MARK_NOT_FOUND);
        }

        BookMarkedFeed bookMarkedFeed = bookMarkedFeedRepository.findByUserUsernameAndId(username, bookMarkedFeedId).orElseThrow(() ->
                new BadRequestException(ErrorCode.NOT_USER_BOOK_MARK));

        // 연관관계 제거
        dailyBookMarkedFeedRepository.findByBookMarkedFeedId(bookMarkedFeed.getId())
                .forEach(dailyBookMarkedFeed ->
                        dailyBookMarkedFeed.updateBookMarkedFeed(null));

        bookMarkedFeedRepository.delete(bookMarkedFeed);
    }


}
