package com.petplate.petplate.petfood.service;

import com.petplate.petplate.common.response.error.ErrorCode;
import com.petplate.petplate.common.response.error.exception.BadRequestException;
import com.petplate.petplate.common.response.error.exception.InternalServerErrorException;
import com.petplate.petplate.common.response.error.exception.NotFoundException;
import com.petplate.petplate.petdailymeal.repository.DailyBookMarkedRawRepository;
import com.petplate.petplate.petfood.domain.entity.BookMarkedRaw;
import com.petplate.petplate.petfood.domain.entity.Raw;
import com.petplate.petplate.petfood.dto.request.CreateBookMarkedRawRequestDto;
import com.petplate.petplate.petfood.dto.response.ReadBookMarkedRawResponseDto;
import com.petplate.petplate.petfood.repository.BookMarkedRawRepository;
import com.petplate.petplate.petfood.repository.RawRepository;
import com.petplate.petplate.user.domain.entity.User;
import com.petplate.petplate.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookMarkedRawService {
    private final UserRepository userRepository;
    private final RawRepository rawRepository;
    private final BookMarkedRawRepository bookMarkedRawRepository;
    private final DailyBookMarkedRawRepository dailyBookMarkedRawRepository;

    @Transactional
    public Long createBookMarkedRaw(String username, CreateBookMarkedRawRequestDto requestDto) {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new NotFoundException(ErrorCode.USER_NOT_FOUND)
        );

        Raw raw = rawRepository.findById(requestDto.getRawId()).orElseThrow(
                () -> new NotFoundException(ErrorCode.RAW_NOT_FOUND));

        // 즐겨찾기에 새로 추가하고자 하는 것과 식품, 섭취량에 대해 동일한 내용이 존재한다면 오류가 발생함
        bookMarkedRawRepository.findByUserIdAndRawIdAndServing(user.getId(), requestDto.getRawId(), requestDto.getServing()).ifPresent(bookMarkedRaw -> {
            throw new BadRequestException(ErrorCode.BOOK_MARK_ALREADY_EXISTS);
        });

        BookMarkedRaw bookMarkedRaw = BookMarkedRaw.builder().raw(raw)
                .user(user)
                .serving(requestDto.getServing())
                .build();

        bookMarkedRawRepository.save(bookMarkedRaw);

        return bookMarkedRaw.getId();
    }

    public List<ReadBookMarkedRawResponseDto> getBookMarkedRaws(String username) {
        List<ReadBookMarkedRawResponseDto> responses = new ArrayList<>();
        bookMarkedRawRepository.findByUserUsername(username).forEach(bookMarkedRaw -> {
            responses.add(ReadBookMarkedRawResponseDto.from(bookMarkedRaw));
        });

        return responses;
    }

    public ReadBookMarkedRawResponseDto getBookMarkedRaw(String username, Long bookMarkedRawId) {
        BookMarkedRaw bookMarkedRaw = bookMarkedRawRepository.findById(bookMarkedRawId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.BOOK_MARK_NOT_FOUND));

        if (!Objects.equals(username, bookMarkedRaw.getUser().getUsername())) {
            throw new BadRequestException(ErrorCode.NOT_USER_BOOK_MARK);
        }

        return ReadBookMarkedRawResponseDto.from(bookMarkedRaw);
    }

    @Transactional
    public void deleteBookMarkedRaw(String username, Long bookMarkedRawId) {
        BookMarkedRaw bookMarkedRaw = bookMarkedRawRepository.findById(bookMarkedRawId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.BOOK_MARK_NOT_FOUND));

        if (!bookMarkedRaw.getUser().getUsername().equals(username)) {
            throw new BadRequestException(ErrorCode.NOT_USER_BOOK_MARK);
        }

        // 연관관계 제거
        dailyBookMarkedRawRepository.findByBookMarkedRawId(bookMarkedRawId)
                .forEach(dailyBookMarkedRaw -> dailyBookMarkedRaw.updateBookMarkedRaw(null));

        bookMarkedRawRepository.delete(bookMarkedRaw);
    }

}
