package com.petplate.petplate.petfood.service;

import com.petplate.petplate.common.response.error.ErrorCode;
import com.petplate.petplate.common.response.error.exception.BadRequestException;
import com.petplate.petplate.common.response.error.exception.NotFoundException;
import com.petplate.petplate.petdailymeal.repository.DailyBookMarkedRawRepository;
import com.petplate.petplate.petfood.domain.entity.BookMarkedRaw;
import com.petplate.petplate.petfood.domain.entity.Raw;
import com.petplate.petplate.petfood.dto.request.CreateBookMarkedRawRequest;
import com.petplate.petplate.petfood.repository.BookMarkedRawRepository;
import com.petplate.petplate.petfood.repository.RawRepository;
import com.petplate.petplate.user.domain.entity.User;
import com.petplate.petplate.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookMarkedRawService {
    private final UserRepository userRepository;
    private final RawRepository rawRepository;
    private final BookMarkedRawRepository bookMarkedRawRepository;
    private final DailyBookMarkedRawRepository dailyBookMarkedRawRepository;

    @Transactional
    public Long createBookMarkedRaw(String username, CreateBookMarkedRawRequest requestDto) {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new NotFoundException(ErrorCode.NOT_FOUND)
        );

        Raw raw = rawRepository.findById(requestDto.getRawId()).orElseThrow(
                () -> new NotFoundException(ErrorCode.RAW_NOT_FOUND));

        // 즐겨찾기에 새로 추가하고자 하는 것과 식품, 섭취량에 대해 동일한 내용이 존재한다면 오류가 발생함
        bookMarkedRawRepository.findByUserIdAndRawId(user.getId(), raw.getId()).ifPresent(bookMarkedRaw -> {
            if (bookMarkedRaw.getServing() == requestDto.getServing())
                new BadRequestException(ErrorCode.BOOK_MARK_ALREADY_EXISTS);
        });

        BookMarkedRaw bookMarkedRaw = BookMarkedRaw.builder().raw(raw)
                .user(user)
                .build();

        bookMarkedRawRepository.save(bookMarkedRaw);

        return bookMarkedRaw.getId();
    }

    @Transactional
    public void deleteBookMarkedRaw(Long bookMarkedRawId) {
        BookMarkedRaw bookMarkedRaw = bookMarkedRawRepository.findById(bookMarkedRawId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));

        // pk가 -1인 BookMarkedRaw
        BookMarkedRaw noDataExistBookMark = bookMarkedRawRepository.findById(-1L).orElseThrow(() -> new NotFoundException(ErrorCode.BOOK_MARK_NOT_FOUND));

        dailyBookMarkedRawRepository.findByBookMarkedRawId(bookMarkedRawId)
                .forEach(dailyBookMarkedRaw -> dailyBookMarkedRaw.updateBookMarkedRaw(noDataExistBookMark));

        bookMarkedRawRepository.delete(bookMarkedRaw);
    }

}
