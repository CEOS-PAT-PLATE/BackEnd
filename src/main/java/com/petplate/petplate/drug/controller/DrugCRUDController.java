package com.petplate.petplate.drug.controller;

import com.petplate.petplate.common.response.BaseResponse;
import com.petplate.petplate.drug.dto.request.DrugSaveRequestDto;
import com.petplate.petplate.drug.dto.response.DrugResponseDto;
import com.petplate.petplate.drug.dto.response.ShowNutrientListResponseDto;
import com.petplate.petplate.drug.service.DrugCRUDService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/drug")
public class DrugCRUDController {

    private final DrugCRUDService drugCRUDService;

    @PostMapping
    public ResponseEntity<Void> saveDrug(@RequestBody @Valid DrugSaveRequestDto drugSaveRequestDto){

        drugCRUDService.saveDrug(drugSaveRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DrugResponseDto> showDrug(@PathVariable("id") Long drugId){

        return ResponseEntity.ok(drugCRUDService.showDrug(drugId));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDrug(@PathVariable("id")Long drugId){
        drugCRUDService.deleteDrug(drugId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/standard-nutrients")
    public ResponseEntity<BaseResponse<ShowNutrientListResponseDto>> showAllNutrients(){

        return ResponseEntity.ok(BaseResponse.createSuccess(drugCRUDService.showAllNutrientName()));
    }


}
