package com.petplate.petplate.petfood.controller;

import com.petplate.petplate.auth.interfaces.CurrentUserUsername;
import com.petplate.petplate.common.response.BaseResponse;
import com.petplate.petplate.petdailymeal.dto.request.CreateDailyRawRequestDto;
import com.petplate.petplate.petdailymeal.dto.response.ReadDailyRawResponseDto;
import com.petplate.petplate.petdailymeal.dto.response.ReadDailyRawWithRawIdResponseDto;
import com.petplate.petplate.petdailymeal.service.DailyMealService;
import com.petplate.petplate.petdailymeal.service.DailyRawService;
import com.petplate.petplate.petfood.dto.request.CreateRawRequestDto;
import com.petplate.petplate.petfood.dto.response.ReadRawResponseDto;
import com.petplate.petplate.petfood.service.RawService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

    @RestController
    @RequestMapping("/api/v1")
    @RequiredArgsConstructor
    @Tag(name = "자연식 컨트롤러", description = "자연식 및 펫이 하룻 동안 섭취한 자연식에 대한 컨트롤러 입니다")
    public class RawController {
        private final RawService rawService;
        private final DailyMealService dailyMealService;
        private final DailyRawService dailyRawService;

        private static final String OK = "200";
        private static final String CREATED = "201";
        private static final String BAD_REQUEST = "400";
        private static final String NOT_FOUND = "404";

    @Operation(summary = "자연식 추가")
    @ApiResponses(value = {
            @ApiResponse(responseCode = CREATED, description = "자연식 성공적 추가"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "동명의 자연식이 존재")
    })
    @PostMapping("/raws")
    public ResponseEntity<BaseResponse<Long>> createRaw(@RequestBody @Valid CreateRawRequestDto requestDto) {
        Long rawId = rawService.createRaw(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.createSuccess(rawId));
    }

    @Operation(summary = "rawId로 자연식 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "자연식 성공적 조회"),
            @ApiResponse(responseCode = NOT_FOUND, description = "존재하지 않은 자연식")
    })
    @GetMapping("/raws/{rawId}")
    public ResponseEntity<BaseResponse<ReadRawResponseDto>> readRaw(@PathVariable("rawId") Long rawId) {
        ReadRawResponseDto response = rawService.getRaw(rawId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.createSuccess(response));
    }

    @Operation(summary = "keyword가 포함된 자연식 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "자연식 성공적 조회")
    })
    @GetMapping("/raws")
    public ResponseEntity<BaseResponse<List<ReadRawResponseDto>>> readRawByKeyword(@RequestParam("keyword") String keyword) {
        List<ReadRawResponseDto> responses = rawService.getRawByKeyword(keyword);

        for (ReadRawResponseDto response : responses) {
            System.out.println("response.getName() = " + response.getName());
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.createSuccess(responses));
    }

    @Operation(summary = "자연식 제거")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "자연식 성공적 제거"),
            @ApiResponse(responseCode = NOT_FOUND, description = "존재하지 않은 자연식")
    })
    @DeleteMapping("/raws")
    public ResponseEntity<BaseResponse> deleteRaw(@RequestParam("rawId") Long rawId) {
        rawService.deleteRawById(rawId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.createSuccess(null));
    }

    @Operation(summary = "오늘 식사내역에 자연식을 저장")
    @ApiResponses(value = {
            @ApiResponse(responseCode = CREATED, description = "섭취 자연식 성공적 저장"),
            @ApiResponse(responseCode = NOT_FOUND, description = "자연식 ID(rawId)가 제대로 입력되지 않음")
    })
    @PostMapping("/pets/{petId}/raws")
    public ResponseEntity<BaseResponse<Long>> createDailyRaw(@CurrentUserUsername String username, @PathVariable("petId") Long petId, @Valid @RequestBody CreateDailyRawRequestDto requestDto) {
        Long dailyRawId = dailyRawService.createDailyRaw(username, petId, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.createSuccess(dailyRawId));
    }

    @Operation(summary = "가장 최근 두번의 식사에서 섭취한 자연식들 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "섭취 자연식 성공적 조회"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "유저의 반려견이 아님"),
            @ApiResponse(responseCode = NOT_FOUND, description = "존재하지 않은 반려견")
    })
    @GetMapping("/pets/{petId}/raws/recent")
    public ResponseEntity<BaseResponse<List<ReadDailyRawWithRawIdResponseDto>>> getRecentDailyRaws(@CurrentUserUsername String username, @PathVariable("petId") Long petId) {
        int days = 2;
        List<ReadDailyRawWithRawIdResponseDto> recentDailyRaws = dailyRawService.getRecentDailyRaws(username, petId, days);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.createSuccess(recentDailyRaws));
    }

    @Operation(summary = "섭취했던 자연식 단건 조회", description = "자연식의 섭취 내역을 조회합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "섭취 자연식 성공적 조회"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "유저의 반려견이 아님"),
            @ApiResponse(responseCode = NOT_FOUND, description = "존재하지 않은 반려견 혹은 존재하지 않은 식사 내역")
    })
    @GetMapping("/pets/{petId}/raws/{dailyRawId}")
    public ResponseEntity<BaseResponse<ReadDailyRawResponseDto>> getDailyRaw(@CurrentUserUsername String username, @PathVariable("petId") Long petId, @PathVariable("dailyRawId") Long dailyRawId) {
        ReadDailyRawResponseDto dailyRaw = dailyRawService.getDailyRaw(username, petId, dailyRawId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.createSuccess(dailyRaw));
    }

    @Operation(summary = "섭취했던 자연식 제거", description = "자연식의 섭취 내역을 제거합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "섭취 자연식 성공적 조회"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "유저의 반려견이 아님"),
            @ApiResponse(responseCode = NOT_FOUND, description = "존재하지 않은 반려견 혹은 존재하지 않은 식사 내역")
    })
    @DeleteMapping("/pets/{petId}/raws/{dailyRawId}")
    public ResponseEntity<BaseResponse> deleteDailyRaw(@CurrentUserUsername String username, @PathVariable("petId") Long petId, @PathVariable("dailyRawId") Long dailyRawId) {
        dailyRawService.deleteDailyRaw(username, petId, dailyRawId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.createSuccess(null));
    }

    @Operation(summary = "특정 식사에서 섭취한 자연식들 제거")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "자연식 성공적 제거"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "본인의 반려견이 아닌 경우, 해당 반려견의 식사 내역이 아닌 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "잘못된 petId, 잘못된 dailyMealId")
    })
    @DeleteMapping("/pet/{petId}/dailyMeals/{dailyMealId}/raws")
    public ResponseEntity<BaseResponse> deleteDailyRaws(@CurrentUserUsername String username, @PathVariable("petId") Long petId, @PathVariable("dailyMealId") Long dailyMealId) {
        dailyRawService.deleteDailyRaws(username, petId, dailyMealId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.createSuccessWithNoContent());
    }
}
