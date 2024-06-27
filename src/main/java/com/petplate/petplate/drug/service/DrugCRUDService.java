package com.petplate.petplate.drug.service;

import static java.util.Arrays.stream;

import com.petplate.petplate.common.EmbeddedType.StandardNutrient;
import com.petplate.petplate.common.response.error.ErrorCode;
import com.petplate.petplate.common.response.error.exception.NotFoundException;
import com.petplate.petplate.drug.domain.entity.Drug;
import com.petplate.petplate.drug.dto.request.DrugSaveRequestDto;
import com.petplate.petplate.drug.dto.response.DrugResponseDto;
import com.petplate.petplate.drug.dto.response.ShowNutrientListResponseDto;
import com.petplate.petplate.drug.repository.DrugRepository;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DrugCRUDService {

    private final DrugRepository drugRepository;



    //drug 생성
    @Transactional
    public void saveDrug(final DrugSaveRequestDto drugSaveRequestDto){


        Drug drug = Drug.builder()
                .name(drugSaveRequestDto.getName())
                .englishName(drugSaveRequestDto.getEnglishName())
                .vendor(drugSaveRequestDto.getVendor())
                .drugImgPath(drugSaveRequestDto.getDrugImgPath())
                .url(drugSaveRequestDto.getUrl())
                .efficientNutrient(drugSaveRequestDto.getEfficientNutrients().stream().map(this::toStandardNutrient).collect(
                        Collectors.toSet()))
                .build();

        drugRepository.save(drug);
    }


    //drug 삭제
    @Transactional
    public void deleteDrug(final Long drugId){

        drugRepository.deleteById(drugId);
    }

    //단일 약 보여주기
    public DrugResponseDto showDrug(final Long drugId){

        return DrugResponseDto.of(findDrugById(drugId));
    }


    //String -> Enum 타입 변환
    private StandardNutrient toStandardNutrient(final String nutrients) {

        return Arrays.stream(StandardNutrient.values())
                .filter(StandardNutrient-> StandardNutrient.getName().equals(nutrients))
                .findAny()
                .orElseThrow(()->new NotFoundException(ErrorCode.NUTRIENT_NOT_FOUND));
    }

    // Enum 타입의 영양소 이름 출력
    public ShowNutrientListResponseDto showAllNutrientName(){

        return ShowNutrientListResponseDto.of(stream(StandardNutrient.values()).map(StandardNutrient::getName).collect(
                Collectors.toList()));
    }


    //id 로 drug 가져오기
    private Drug findDrugById(Long drugId){
        return drugRepository.findById(drugId).orElseThrow(()->new NotFoundException(
                ErrorCode.DRUG_NOT_FOUND
        ));
    }

}
