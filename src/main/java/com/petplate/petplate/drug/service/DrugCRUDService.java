package com.petplate.petplate.drug.service;

import static java.util.Arrays.stream;

import com.petplate.petplate.common.EmbeddedType.StandardNutrient;
import com.petplate.petplate.common.response.error.ErrorCode;
import com.petplate.petplate.common.response.error.exception.NotFoundException;
import com.petplate.petplate.drug.domain.entity.Drug;
import com.petplate.petplate.drug.domain.entity.DrugDrugUsefulPart;
import com.petplate.petplate.drug.domain.entity.DrugNutrient;
import com.petplate.petplate.drug.domain.entity.DrugUsefulPart;
import com.petplate.petplate.drug.dto.request.DrugSaveRequestDto;
import com.petplate.petplate.drug.dto.response.DrugResponseDto;
import com.petplate.petplate.drug.dto.response.DrugUsefulPartResponseDto;
import com.petplate.petplate.drug.dto.response.ShowNutrientListResponseDto;
import com.petplate.petplate.drug.repository.DrugDrugUsefulPartRepository;
import com.petplate.petplate.drug.repository.DrugNutrientRepository;
import com.petplate.petplate.drug.repository.DrugRepository;
import com.petplate.petplate.drug.repository.DrugUsefulPartRepository;
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
    private final DrugUsefulPartRepository drugUsefulPartRepository;
    private final DrugDrugUsefulPartRepository drugDrugUsefulPartRepository;



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

        //drug 단일 저장
        Drug savedDrug = drugRepository.save(drug);

        //drug 연관관계 저장- DrugNutrient (Drug 와 영양 성분)
        drugSaveRequestDto.getEfficientNutrients().forEach(nutrient->{

            DrugNutrient drugNutrient = DrugNutrient.builder()
                            .standardNutrient(toStandardNutrient(nutrient))
                                    .drug(drug)
                                            .build();

            drugNutrientRepository.save(drugNutrient);
            savedDrug.getDrugNutrientList().add(drugNutrient);
        });

        //drug 연관관계 저장 - Drug와 DrugUsefulPart (Drug 와 도움이 되는 부분)
        drugSaveRequestDto.getDrugUsefulPartList().forEach(drugUsefulPartId->{

            DrugUsefulPart drugUsefulPart = drugUsefulPartRepository.findById(drugUsefulPartId)
                    .orElseThrow(()-> new NotFoundException(ErrorCode.DRUG_USEFUL_PART_NOT_FOUND));

            DrugDrugUsefulPart drugDrugUsefulPart = DrugDrugUsefulPart.builder()
                    .drugUsefulPart(drugUsefulPart)
                    .drug(savedDrug)
                    .build();

            drugDrugUsefulPartRepository.save(drugDrugUsefulPart);
            savedDrug.getDrugDrugUsefulPartList().add(drugDrugUsefulPart);
        });

        return savedDrug.getId();

    }



    //단일 약 보여주기
    public DrugResponseDto showDrug(final Long drugId){


        //약과 연관된 영양 성분 이름 추출
        List<String> nutrientsName = drugNutrientRepository.findByDrugIdWithFetchDrug(drugId).stream()
                .map(drugNutrient ->drugNutrient.getStandardNutrient().getName()
        ).collect(Collectors.toList());

        //약과 연관된 도움되는 부분 이름 추출
        List<String> drugUsefulPartsName = drugDrugUsefulPartRepository
                .findByDrugIdWithFetchDrugUsefulPart(drugId).stream().map(drugDrugUsefulPart -> drugDrugUsefulPart.getDrugUsefulPart().getName())
                .collect(Collectors.toList());

        Drug findDrug = findDrugById(drugId);

        return DrugResponseDto.of(findDrug,nutrientsName,drugUsefulPartsName);
    }


    @Transactional
    public void deleteDrug(final Long drugId){

        existsDrugId(drugId);

        drugNutrientRepository.deleteByDrugId(drugId);
        drugDrugUsefulPartRepository.deleteByDrugId(drugId);
        drugRepository.deleteById(drugId);
    }

    public List<DrugResponseDto> showAllDrug(){

        List<DrugResponseDto> drugResponseDtoList = drugRepository.findAll()
                .stream()
                .map(drug->DrugResponseDto.of(drug,
                drug.getDrugNutrientList()
                        .stream()
                        .map(drugNutrient -> drugNutrient.getStandardNutrient().getName()).collect(Collectors.toList()),
                        drugDrugUsefulPartRepository.findByDrugIdWithFetchDrugUsefulPart(drug.getId())
                                .stream()
                                .map(drugDrugUsefulPart -> drugDrugUsefulPart.getDrugUsefulPart().getName()).collect(Collectors.toList()))).collect(Collectors.toList());

        return drugResponseDtoList;
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
