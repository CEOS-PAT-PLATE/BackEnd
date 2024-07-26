package com.petplate.petplate.drug.service;

import com.petplate.petplate.common.response.error.ErrorCode;
import com.petplate.petplate.common.response.error.exception.BadRequestException;
import com.petplate.petplate.common.response.error.exception.NotFoundException;
import com.petplate.petplate.drug.domain.entity.DrugUsefulPart;
import com.petplate.petplate.drug.dto.request.DrugUsefulPartSaveRequestDto;
import com.petplate.petplate.drug.dto.response.DrugUsefulPartResponseDto;
import com.petplate.petplate.drug.repository.DrugUsefulPartRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DrugUsefulPartService {

    private final DrugUsefulPartRepository drugUsefulPartRepository;



    @Transactional
    public Long saveDrugUsefulPart(final DrugUsefulPartSaveRequestDto drugUsefulPartSaveRequestDto){

        if(drugUsefulPartRepository.existsByName(drugUsefulPartSaveRequestDto.getName())){
            throw new BadRequestException(ErrorCode.DRUG_USEFUL_PART_ALREADY_EXIST);
        }

        DrugUsefulPart drugUsefulPart = DrugUsefulPart.builder()
                .name(drugUsefulPartSaveRequestDto.getName())
                .build();

        drugUsefulPartRepository.save(drugUsefulPart);

        return drugUsefulPart.getId();
    }

    public List<DrugUsefulPartResponseDto> showAllDrugUsefulPart(){

        return drugUsefulPartRepository.findAll()
                .stream()
                .map(DrugUsefulPartResponseDto::of).collect(Collectors.toList());
    }

    @Transactional
    public void deleteDrugUsefulPart(final Long drugUsefulPartId){

        if(!drugUsefulPartRepository.existsById(drugUsefulPartId)){
            throw new NotFoundException(ErrorCode.DRUG_USEFUL_PART_NOT_FOUND);
        }

        drugUsefulPartRepository.deleteById(drugUsefulPartId);

    }




}
