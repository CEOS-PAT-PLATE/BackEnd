package com.petplate.petplate.drug.controller;

import com.petplate.petplate.common.response.BaseResponse;
import com.petplate.petplate.drug.dto.request.DrugFindRequestDto;
import com.petplate.petplate.drug.dto.response.DrugResponseDto;
import com.petplate.petplate.drug.dto.response.RecommendDrugResponseDto;
import com.petplate.petplate.drug.service.DrugRecommendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/drug/recommend")
@Tag(name = "영양제 추천 컨트롤러", description = "영양제 추천을  위한 컨트롤러 입니다. 다만 영양제 추천은 컨트롤러를 통해 조회하지 않을 예정이므로"
        + "테스트 용도로 파악해주십시오!")
public class DrugRecommendController {

    private final DrugRecommendService drugRecommendService;

    @GetMapping("/proper")
    @Operation(summary = "단일 영양소 기반 추천 영양제",description = "단일 영양소를 기반으로 영양제를 추천합니다 - 컨트롤러를 만들 필요 없을 가능성이 높으나 그냥 만들어봅니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "단일 영양소 기반 추천 영양제 성공"),
            @ApiResponse(responseCode = "404",description = "영양소 이름을 잘못 입력했을 때")
    })
    @Parameters({
            @Parameter(in = ParameterIn.QUERY, name = "nutrient", description = "단일 영양소 이름", required = true)
    })
    public ResponseEntity<BaseResponse<List<RecommendDrugResponseDto>>> showProperNutrients(@RequestParam("nutrient") final String nutrient){

        return ResponseEntity.ok(BaseResponse.createSuccess(drugRecommendService.findDrugByNutrientName(nutrient)));
    }

    @PostMapping("/proper2")
    @Operation(summary = "다중 영양소 기반 추천 영양제",description = "다중 영양소를 기반으로 영양제를 추천합니다 - 컨트롤러를 만들 필요 없을 가능성이 높으나 그냥 만들어봅니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "다중 영양소 기반 추천 영양제 성공"),
            @ApiResponse(responseCode = "404",description = "영양소 이름을 잘못 입력했을 때")
    })
    public ResponseEntity<BaseResponse<List<RecommendDrugResponseDto>>> showProperNutrients2(@RequestBody @Valid
            final DrugFindRequestDto drugFindRequestDto){

        return ResponseEntity.ok(BaseResponse.createSuccess(drugRecommendService.findDrugByVariousNutrientName(drugFindRequestDto)));

    }



}
