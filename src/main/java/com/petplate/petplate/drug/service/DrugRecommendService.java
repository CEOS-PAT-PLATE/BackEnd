package com.petplate.petplate.drug.service;

import com.petplate.petplate.common.EmbeddedType.StandardNutrient;
import com.petplate.petplate.common.response.error.ErrorCode;
import com.petplate.petplate.common.response.error.exception.NotFoundException;
import com.petplate.petplate.drug.domain.entity.Drug;
import com.petplate.petplate.drug.dto.request.DrugFindRequestDto;
import com.petplate.petplate.drug.dto.response.DrugResponseDto;
import com.petplate.petplate.drug.repository.DrugNutrientRepository;
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
public class DrugRecommendService {

    private final DrugRepository drugRepository;
    private final DrugNutrientRepository drugNutrientRepository;

    /*
     테스트 용도로 만든 메서드입니다.
     이런 식으로 특정 영양소에 맞는 Drug List 를 가지고 올 수 있습니다 .
     */
    public List<DrugResponseDto> findDrugByNutrientName(final String nutrientName){

        StandardNutrient standardNutrient = toStandardNutrient(nutrientName);

        List<Drug> findSuitableDrugList = drugNutrientRepository.findByStandardNutrientWithFetchDrug(standardNutrient).stream()
                .map(drugNutrient -> drugNutrient.getDrug()).collect(Collectors.toList());

        return findSuitableDrugList.stream().map(drug-> DrugResponseDto.of(drug,drugNutrientRepository.findByDrug(drug).stream()
                .map(drugNutrient -> drugNutrient.getStandardNutrient().getName()).collect(
                        Collectors.toList()))).collect(Collectors.toList());
    }

    public List<DrugResponseDto> findDrugByVariousNutrientName(final DrugFindRequestDto drugFindRequestDto){

        List<StandardNutrient> standardNutrientList = drugFindRequestDto.getNutrients().stream().map(this::toStandardNutrient).collect(
                Collectors.toList());


        List<DrugResponseDto> suitableDrugResponseDtoList = drugRepository.findUserProperDrugList(standardNutrientList).stream()
                .map(drug->DrugResponseDto.of(drug,drug.getDrugNutrientList().stream().map(drugNutrient ->
                        drugNutrient.getStandardNutrient().getName()).collect(Collectors.toList())))
                .collect(Collectors.toList());

        return suitableDrugResponseDtoList;

    }

    private StandardNutrient toStandardNutrient(final String nutrients) {

        return Arrays.stream(StandardNutrient.values())
                .filter(StandardNutrient-> StandardNutrient.getName().equals(nutrients))
                .findAny()
                .orElseThrow(()->new NotFoundException(ErrorCode.NUTRIENT_NOT_FOUND));
    }

}
