package com.petplate.petplate.drug.controller;

import com.petplate.petplate.common.response.BaseResponse;
import com.petplate.petplate.drug.dto.request.DrugSaveRequestDto;
import com.petplate.petplate.drug.dto.request.DrugUsefulPartSaveRequestDto;
import com.petplate.petplate.drug.dto.response.DrugResponseDto;
import com.petplate.petplate.drug.dto.response.DrugUsefulPartResponseDto;
import com.petplate.petplate.drug.service.DrugUsefulPartService;
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
@RequestMapping("/api/v1/drugUsefulPart")
@Tag(name = "영양제가 도움되는 부분에 대한 컨트롤러", description = "영양제가 도움되는 부분에 대한 컨트롤러(저장, 삭제 , 리스트 조회 가능) 입니다")
public class DrugUsefulPartController {

    private final DrugUsefulPartService drugUsefulPartService;


    @PostMapping
    @Operation(summary = "영양제가 도움되는 부분 저장",description = " 관리자 계정 에서만 접근 가능하도록 만들 것입니다. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",description = "영양제가 도움되는 부분 성공적 저장"),
            @ApiResponse(responseCode = "400",description = "영양제가 도움되는 부분 이름이 이미 존재합니다.")
    })
    public ResponseEntity<BaseResponse<Long>> saveDrugUsefulPart(@RequestBody @Valid final
            DrugUsefulPartSaveRequestDto drugUsefulPartSaveRequestDto){

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.createSuccess(drugUsefulPartService.saveDrugUsefulPart(drugUsefulPartSaveRequestDto)));

    }


    @GetMapping()
    @Operation(summary = "영양제가 도움되는 부분 리스트 조회",description = "영양제가 도움되는 부분의 종류를 확인합니다. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "영양제가 도움되는 부분 리스트 성공적 조회"),
    })
    public ResponseEntity<BaseResponse<List<DrugUsefulPartResponseDto>>> showDrugUsefulPartList(){

        return ResponseEntity.ok(BaseResponse.createSuccess(drugUsefulPartService.showAllDrugUsefulPart()));
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "영양제가 도움되는 부분 삭제",description = "영양제가 도움되는 부분의 아이디를 통해 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "영양제가 도움되는 부분 아이디 기반 삭제"),
            @ApiResponse(responseCode = "400",description = "영양제가 도움되는 부분 아이디 조회 실패"),
    })
    @Parameters({
            @Parameter(in = ParameterIn.PATH,name = "id", description = "영양제가 도움되는 부분에 대한 아이디", required = true , example = "1")
    })
    public ResponseEntity<Void> deleteDrugUsefulPart(@PathVariable("id") final Long drugUsefulPartId){

        drugUsefulPartService.deleteDrugUsefulPart(drugUsefulPartId);

        return ResponseEntity.ok().build();
    }



}
