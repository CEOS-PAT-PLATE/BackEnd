package com.petplate.petplate.drug.service;

import static java.util.Arrays.stream;

import com.petplate.petplate.common.EmbeddedType.StandardNutrient;
import com.petplate.petplate.common.response.error.ErrorCode;
import com.petplate.petplate.common.response.error.exception.NotFoundException;
import com.petplate.petplate.drug.domain.entity.Drug;
import com.petplate.petplate.drug.domain.entity.DrugNutrient;
import com.petplate.petplate.drug.dto.request.DrugSaveRequestDto;
import com.petplate.petplate.drug.dto.response.DrugResponseDto;
import com.petplate.petplate.drug.dto.response.ShowNutrientListResponseDto;
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
public class DrugCRUDService {

    private final DrugRepository drugRepository;
    private final DrugNutrientRepository drugNutrientRepository;



    //drug 생성
    @Transactional
    public Long saveDrug(final DrugSaveRequestDto drugSaveRequestDto){

        Drug drug = Drug.builder()
                .name(drugSaveRequestDto.getName())
                .englishName(drugSaveRequestDto.getEnglishName())
                .vendor(drugSaveRequestDto.getVendor())
                .drugImgPath(drugSaveRequestDto.getDrugImgPath())
                .url(drugSaveRequestDto.getUrl())
                .build();

        Drug savedDrug = drugRepository.save(drug);

        drugSaveRequestDto.getEfficientNutrients().forEach(nutrient->{

            DrugNutrient drugNutrient = DrugNutrient.builder()
                            .standardNutrient(toStandardNutrient(nutrient))
                                    .drug(drug)
                                            .build();

            drugNutrientRepository.save(drugNutrient);
        });

        return savedDrug.getId();

    }



    //단일 약 보여주기
    public DrugResponseDto showDrug(final Long drugId){


        List<String> nutrientsName = drugNutrientRepository.findByDrugIdWithFetchDrug(drugId).stream()
                .map(drugNutrient ->drugNutrient.getStandardNutrient().getName()
        ).collect(Collectors.toList());

        Drug findDrug = findDrugById(drugId);

        return DrugResponseDto.of(findDrug,nutrientsName);
    }


    @Transactional
    public void deleteDrug(final Long drugId){

        existsDrugId(drugId);

        drugNutrientRepository.deleteByDrugId(drugId);
        drugRepository.deleteById(drugId);
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

        return ShowNutrientListResponseDto.from(stream(StandardNutrient.values()).map(StandardNutrient::getName).collect(
                Collectors.toList()));
    }


    //id 기반 Drug 존재하는지 판단.
    private void existsDrugId(final Long drugId){
        if(!drugRepository.existsById(drugId)){
            throw new NotFoundException(ErrorCode.DRUG_NOT_FOUND);
        }
    }


    //id 로 drug 가져오기
    private Drug findDrugById(final Long drugId){
        return drugRepository.findById(drugId).orElseThrow(()->new NotFoundException(
                ErrorCode.DRUG_NOT_FOUND
        ));
    }

}
