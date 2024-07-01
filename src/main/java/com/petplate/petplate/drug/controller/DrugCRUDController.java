package com.petplate.petplate.drug.controller;

import com.petplate.petplate.common.response.BaseResponse;
import com.petplate.petplate.drug.dto.request.DrugSaveRequestDto;
import com.petplate.petplate.drug.dto.response.DrugResponseDto;
import com.petplate.petplate.drug.dto.response.ShowNutrientListResponseDto;
import com.petplate.petplate.drug.service.DrugCRUDService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "영양제 기본 컨트롤러", description = "영양제 등록, 조회, 수정, 삭제를 위한 컨트롤러 입니다")
public class DrugCRUDController {

    private final DrugCRUDService drugCRUDService;

    @PostMapping
    @Operation(summary = "영양제 저장",description = "추후 관리자 계정 에서만 접근 가능하도록 만들 것입니다. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",description = "영양제 성공적 저장"),
            @ApiResponse(responseCode = "404",description = "영양소 이름이 제대로 입력되지 않았습니다.")
    })
    public ResponseEntity<BaseResponse<Long>> saveDrug(@RequestBody @Valid final DrugSaveRequestDto drugSaveRequestDto){

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.createSuccess(drugCRUDService.saveDrug(drugSaveRequestDto)));

    }

    @GetMapping("/{id}")
    @Operation(summary = "영양제 단일 조회",description = "테스트 - 영양제가 성공적으로 저장되어 있는지 판단합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "단일 영양제 성공적 조회"),
            @ApiResponse(responseCode = "404",description = "영양제가 존재하지 않을때")
    })
    @Parameters({
            @Parameter(in = ParameterIn.PATH,name = "id", description = "단일 조회시 사용하는 영양제 아이디", required = true , example = "1")
    })
    public ResponseEntity<BaseResponse<DrugResponseDto>> showDrug(@PathVariable("id") final Long drugId){

        return ResponseEntity.ok(BaseResponse.createSuccess(drugCRUDService.showDrug(drugId)));
    }


    @GetMapping("/standard-nutrients")
    @Operation(summary = "영양소 리스트",description = "영양제 저장시에 사용되며 입력 가능한 영양소를 확인할 수 있습니다. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "모든 영양소 조회"),
    })
    public ResponseEntity<BaseResponse<ShowNutrientListResponseDto>> showAllNutrients(){

        return ResponseEntity.ok(BaseResponse.createSuccess(drugCRUDService.showAllNutrientName()));
    }


}
