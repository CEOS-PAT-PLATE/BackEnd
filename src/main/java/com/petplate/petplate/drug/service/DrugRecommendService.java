package com.petplate.petplate.drug.service;

import com.petplate.petplate.common.EmbeddedType.StandardNutrient;
import com.petplate.petplate.common.response.error.ErrorCode;
import com.petplate.petplate.common.response.error.exception.NotFoundException;
import com.petplate.petplate.dailyMealNutrient.service.DeficientNutrientService;
import com.petplate.petplate.drug.domain.entity.Drug;
import com.petplate.petplate.drug.dto.request.DrugFindRequestDto;
import com.petplate.petplate.drug.dto.response.DrugResponseDto;
import com.petplate.petplate.drug.dto.response.RecommendDrugResponseDto;
import com.petplate.petplate.drug.dto.response.RecommendDrugResponseDtoWithNutrientName;
import com.petplate.petplate.drug.repository.DrugNutrientRepository;
import com.petplate.petplate.drug.repository.DrugRepository;
import com.petplate.petplate.pet.dto.response.ReadPetNutrientResponseDto;
import com.petplate.petplate.pet.service.PetService;
import java.time.LocalDate;
import java.util.ArrayList;
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
    private final DeficientNutrientService deficientNutrientService;

    /*
     테스트 용도로 만든 메서드입니다.
     이런 식으로 특정 영양소에 맞는 Drug List 를 가지고 올 수 있습니다 .
     */
    public List<RecommendDrugResponseDto> findDrugByNutrientName(final String nutrientName){

        StandardNutrient standardNutrient = toStandardNutrient(nutrientName);

        List<Drug> findSuitableDrugList = drugNutrientRepository.findByStandardNutrientWithFetchDrug(standardNutrient).stream()
                .map(drugNutrient -> drugNutrient.getDrug()).collect(Collectors.toList());

        return findSuitableDrugList.stream().map(drug-> RecommendDrugResponseDto.from(drug))
                .collect(Collectors.toList());
    }

    public List<RecommendDrugResponseDtoWithNutrientName> findDrugByVariousNutrientName(final DrugFindRequestDto drugFindRequestDto){

        List<StandardNutrient> standardNutrientList = drugFindRequestDto.getNutrients().stream().map(this::toStandardNutrient).collect(
                Collectors.toList());


        List<RecommendDrugResponseDtoWithNutrientName> drugResponseDtoWithNutrientNames = new ArrayList<>();

        standardNutrientList.forEach(standardNutrient -> {

            List<RecommendDrugResponseDto> drugResponseDtoList = drugRepository.findUserProperDrugWithOneNutrient(standardNutrient)
                    .stream().map(RecommendDrugResponseDto::from).collect(Collectors.toList());

            drugResponseDtoWithNutrientNames.add(RecommendDrugResponseDtoWithNutrientName.of(standardNutrient.getName(),drugResponseDtoList));

        });

        return drugResponseDtoWithNutrientNames;

    }

    public List<RecommendDrugResponseDtoWithNutrientName> findDrugByDeficientNutrientsName(String username,Long petId,
            Long dailyMealId){

        List<ReadPetNutrientResponseDto> ReadPetNutrientResponseDtoList = deficientNutrientService.getDeficientNutrients(username, petId, dailyMealId);


        List<StandardNutrient> standardNutrientList = ReadPetNutrientResponseDtoList.stream().map(deficientNutrient->toStandardNutrient(deficientNutrient.getName()))
                .collect(Collectors.toList());


        List<RecommendDrugResponseDtoWithNutrientName> drugResponseDtoWithNutrientNames = new ArrayList<>();

        standardNutrientList.forEach(standardNutrient -> {

            List<RecommendDrugResponseDto> drugResponseDtoList = drugRepository.findUserProperDrugWithOneNutrient(standardNutrient)
                    .stream().map(RecommendDrugResponseDto::from).collect(Collectors.toList());

            drugResponseDtoWithNutrientNames.add(RecommendDrugResponseDtoWithNutrientName.of(standardNutrient.getName(),drugResponseDtoList));

        });

        return drugResponseDtoWithNutrientNames;

    }

    private StandardNutrient toStandardNutrient(final String nutrients) {

        return Arrays.stream(StandardNutrient.values())
                .filter(StandardNutrient-> StandardNutrient.getName().equals(nutrients))
                .findAny()
                .orElseThrow(()->new NotFoundException(ErrorCode.NUTRIENT_NOT_FOUND));
    }

}
