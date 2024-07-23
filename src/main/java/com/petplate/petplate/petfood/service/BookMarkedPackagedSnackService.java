package com.petplate.petplate.petfood.service;

import com.petplate.petplate.common.EmbeddedType.Nutrient;
import com.petplate.petplate.common.EmbeddedType.Vitamin;
import com.petplate.petplate.common.response.error.ErrorCode;
import com.petplate.petplate.common.response.error.exception.BadRequestException;
import com.petplate.petplate.common.response.error.exception.NotFoundException;
import com.petplate.petplate.petdailymeal.repository.DailyBookMarkedPackagedSnackRepository;
import com.petplate.petplate.petfood.domain.entity.BookMarkedPackagedSnack;
import com.petplate.petplate.petfood.dto.request.CreateBookMarkedPackagedSnackRequestDto;
import com.petplate.petplate.petfood.dto.response.ReadBookMarkedPackagedSnackResponseDto;
import com.petplate.petplate.petfood.repository.BookMarkedPackagedSnackRepository;
import com.petplate.petplate.user.domain.entity.User;
import com.petplate.petplate.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookMarkedPackagedSnackService {
    private final BookMarkedPackagedSnackRepository bookMarkedPackagedSnackRepository;
    private final UserRepository userRepository;
    private final DailyBookMarkedPackagedSnackRepository dailyBookMarkedPackagedSnackRepository;

    // 1 IU retinol = 0.3 mcg RAE
    static final double vitaminAIuRetinolPerGram = 3333333.3333333335; // == 10E6 / 0.3

    // 1 IU = 0.025 μg
    static final double vitaminDIuPerGram = 4.0E7; // == 10E6 / 0.025

    // 1 IU (natural) = 0.67 mg Vitamin E (as alpha-tocopherol)
    // 1 IU (synthetic) = 0.45 mg Vitamin E (as alpha-tocopherol)
    static final double vitaminEIuNaturalPerGram = 1492.5373134328358; // == 10E3 / 0.67

    @Transactional
    public Long createBookMarkedPackagedSnack(String username, CreateBookMarkedPackagedSnackRequestDto requestDto) {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new NotFoundException(ErrorCode.USER_NOT_FOUND)
        );

        // 중복되는 이름과 섭취량의 즐겨찾기 사료 존재시 예외 발생
        if (bookMarkedPackagedSnackRepository.existsByUserUsernameAndNameAndServing(username, requestDto.getName(), requestDto.getServing())) {
            throw new BadRequestException(ErrorCode.BOOK_MARK_ALREADY_EXISTS);
        }

        double serving = requestDto.getServing();

        BookMarkedPackagedSnack bookMarkedPackagedSnack = BookMarkedPackagedSnack.builder()
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

        bookMarkedPackagedSnackRepository.save(bookMarkedPackagedSnack);

        return bookMarkedPackagedSnack.getId();
    }

    private double calculateNutritionAmount(double serving, double nutritionPercent) {
        return serving * (nutritionPercent / 100);
    }

    public List<ReadBookMarkedPackagedSnackResponseDto> getBookMarkedPackagedSnacks(String username) {
        List<ReadBookMarkedPackagedSnackResponseDto> responses = new ArrayList<>();
        bookMarkedPackagedSnackRepository.findByUserUsername(username).forEach(bookMarkedPackagedSnack ->
                responses.add(ReadBookMarkedPackagedSnackResponseDto.from(bookMarkedPackagedSnack))
        );

        return responses;
    }

    public ReadBookMarkedPackagedSnackResponseDto getBookMarkedPackagedSnack(String username, Long bookMarkedPackagedSnackId) {
        if (!bookMarkedPackagedSnackRepository.existsById(bookMarkedPackagedSnackId)) {
            throw new NotFoundException(ErrorCode.BOOK_MARK_NOT_FOUND);
        }

        BookMarkedPackagedSnack bookMarkedPackagedSnack = bookMarkedPackagedSnackRepository.findByUserUsernameAndId(username, bookMarkedPackagedSnackId)
                .orElseThrow(() -> new BadRequestException(ErrorCode.NOT_USER_BOOK_MARK));

        return ReadBookMarkedPackagedSnackResponseDto.from(bookMarkedPackagedSnack);
    }

    @Transactional
    public void deleteBookMarkedPackagedSnack(String username, Long bookMarkedPackagedSnackId) {
        if (!bookMarkedPackagedSnackRepository.existsById(bookMarkedPackagedSnackId)) {
            throw new NotFoundException(ErrorCode.BOOK_MARK_NOT_FOUND);
        }

        BookMarkedPackagedSnack bookMarkedPackagedSnack = bookMarkedPackagedSnackRepository.findByUserUsernameAndId(username, bookMarkedPackagedSnackId).orElseThrow(() ->
                new BadRequestException(ErrorCode.NOT_USER_BOOK_MARK));

        // 연관관계 제거
        dailyBookMarkedPackagedSnackRepository.findByBookMarkedPackagedSnackId(bookMarkedPackagedSnack.getId())
                .forEach(dailyBookMarkedPackagedSnack ->
                        dailyBookMarkedPackagedSnack.updateBookMarkedPackagedSnack(null));

        bookMarkedPackagedSnackRepository.delete(bookMarkedPackagedSnack);
    }
}