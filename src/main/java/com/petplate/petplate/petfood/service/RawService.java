package com.petplate.petplate.petfood.service;

import com.petplate.petplate.common.EmbeddedType.Nutrient;
import com.petplate.petplate.common.EmbeddedType.Vitamin;
import com.petplate.petplate.common.response.error.ErrorCode;
import com.petplate.petplate.common.response.error.exception.BadRequestException;
import com.petplate.petplate.common.response.error.exception.NotFoundException;
import com.petplate.petplate.petdailymeal.repository.DailyRawRepository;
import com.petplate.petplate.petfood.domain.entity.Raw;
import com.petplate.petplate.petfood.dto.request.CreateRawRequestDto;
import com.petplate.petplate.petfood.dto.response.ReadRawResponseDto;
import com.petplate.petplate.petfood.repository.BookMarkedRawRepository;
import com.petplate.petplate.petfood.repository.RawRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RawService {
    private final RawRepository rawRepository;
    private final BookMarkedRawRepository BookMarkedRawRepository;
    private final DailyRawRepository dailyRawRepository;
    private final BookMarkedRawRepository bookMarkedRawRepository;

    /**
     * Raw 추가
     * @param requestDto
     * @return id
     */
    @Transactional
    public Long createRaw(CreateRawRequestDto requestDto) {
        if (rawRepository.existsByName(requestDto.getName())) {
            throw new BadRequestException(ErrorCode.RAW_ALREADY_EXISTS);
        }

        Nutrient nutrient = Nutrient.builder()
                .carbonHydrate(requestDto.getCarbonHydrate())
                .protein(requestDto.getProtein())
                .fat(requestDto.getFat())
                .calcium(requestDto.getCalcium())
                .phosphorus(requestDto.getPhosphorus())
                .vitamin(Vitamin.builder()
                        .vitaminA(requestDto.getVitaminA())
                        .vitaminD(requestDto.getVitaminD())
                        .vitaminE(requestDto.getVitaminE())
                        .build())
                .build();

        Raw raw = Raw.builder().name(requestDto.getName())
                .kcal(requestDto.getKcal())
                .standardAmount(requestDto.getStandardAmount())
                .nutrient(nutrient)
                .build();

        rawRepository.save(raw);

        return raw.getId();
    }

    /**
     * PK로 Raw 조회
     * @param rawId
     * @return rawId, standardAmount, name, kcal, carbonHydrate, protein, fat, calcium, phosphorus, vitaminA, vitaminD, vitaminE
     */
    public ReadRawResponseDto getRaw(Long rawId) {
        Raw raw = rawRepository.findById(rawId)
                .orElseThrow(() -> new BadRequestException(ErrorCode.RAW_NOT_FOUND));

        return ReadRawResponseDto.from(raw);
    }

    /**
     * 키워드가 포함된 이름을 가진 Raw 조회
     * @param keyword
     * @return rawId, standardAmount, name, kcal, carbonHydrate, protein, fat, calcium, phosphorus, vitaminA, vitaminD, vitaminE
     */
    public List<ReadRawResponseDto> getRawByKeyword(String keyword) {
        List<ReadRawResponseDto> responses = new ArrayList<>();

        rawRepository.findByKeyword(keyword)
                .forEach(raw -> {
                    responses.add(ReadRawResponseDto.from(raw));
                });

        return responses;
    }

    /**
     * 이름으로 Raw 조회
     * @param name
     * @return rawId, standardAmount, name, kcal, carbonHydrate, protein, fat, calcium, phosphorus, vitaminA, vitaminD, vitaminE
     */
    public ReadRawResponseDto getRawByName(String name) {
        Raw raw = rawRepository.findByName(name).orElseThrow(
                () -> new BadRequestException(ErrorCode.RAW_NOT_FOUND));

        return ReadRawResponseDto.from(raw);
    }

    /**
     * Raw 제거. 연관관계 해제를 위해 해당 Raw와 연관된
     * DailyRaw, BookMarkedDailyRaw의 Raw는 모두 PK가 -1인 Raw로 수정됨.
     * 해당 Raw에는 "존재하지 않은 정보입니다" 이름의 모든 데이터가 0인 정보가 존재하도록 함.
     * @param rawId
     */
    public void deleteRawById(Long rawId) {
        Raw noDataExistRaw = rawRepository.findById(-1L).orElseThrow(() -> new NotFoundException(ErrorCode.RAW_NOT_FOUND));

        dailyRawRepository.findByRawId(rawId).forEach(raw->{
            raw.updateRaw(noDataExistRaw);
        });

        bookMarkedRawRepository.findByRawId(rawId).forEach(raw->{
            raw.updateRaw(noDataExistRaw);
        });

        if (!rawRepository.existsById(rawId)) {
            throw new BadRequestException(ErrorCode.RAW_NOT_FOUND);
        }

        rawRepository.deleteById(rawId);
    }
}
