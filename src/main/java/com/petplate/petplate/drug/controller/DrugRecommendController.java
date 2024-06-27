package com.petplate.petplate.drug.controller;

import com.petplate.petplate.common.response.BaseResponse;
import com.petplate.petplate.drug.dto.response.DrugResponseDto;
import com.petplate.petplate.drug.service.DrugRecommendService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/drug")
public class DrugRecommendController {

    private final DrugRecommendService drugRecommendService;

    @GetMapping("/proper")
    public ResponseEntity<BaseResponse<List<DrugResponseDto>>> showProperNutrients(@Param("nutrient") String nutrient){

        return ResponseEntity.ok(BaseResponse.createSuccess(drugRecommendService.findDrugByNutrientName(nutrient)));
    }

}
