package com.petplate.petplate.petdailymeal.controller;

import com.petplate.petplate.auth.interfaces.CurrentUserUsername;
import com.petplate.petplate.common.response.BaseResponse;
import com.petplate.petplate.petdailymeal.dto.request.CreateDailyPackagedSnackRequestDto;
import com.petplate.petplate.petdailymeal.dto.response.ReadDailyPackagedSnackResponseDto;
import com.petplate.petplate.petdailymeal.service.DailyPackagedSnackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "포장 간식 컨트롤러", description = "반려견이 섭취한 포장 간식에 대한 컨트롤러 입니다")
public class DailyPackagedSnackController {
    private final DailyPackagedSnackService dailyPackagedSnackService;

    private static final String OK = "200";
    private static final String CREATED = "201";
    private static final String BAD_REQUEST = "400";
    private static final String NOT_FOUND = "404";

    @Operation(summary = "섭취 포장 간식 저장", description = "오늘 식사내역에 해당 포장 간식의 정보를 추가합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = CREATED, description = "포장 간식 성공적 추가"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "본인의 반려견이 아닌 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "잘못된 petId")
    })
    @PostMapping("pet/{petId}/packagedSnacks")
    public ResponseEntity<BaseResponse<Long>> createDailyFeed(@CurrentUserUsername String username, @PathVariable("petId") Long petId, @Valid @RequestBody CreateDailyPackagedSnackRequestDto requestDto) {
        Long id = dailyPackagedSnackService.createDailyPackagedSnack(username, petId, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.createSuccess(id));
    }

    @Operation(summary = "섭취 포장 간식 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "포장 간식 성공적 조회"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "본인의 반려견이 아닌 경우, 해당 반려견의 포장 간식이 아닌 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "잘못된 petId, 잘못된 dailyPackagedSnackId")
    })
    @GetMapping("pet/{petId}/packagedSnacks/{dailyPackagedSnackId}")
    public ResponseEntity<BaseResponse<ReadDailyPackagedSnackResponseDto>> readDailyFeed(@CurrentUserUsername String username, @PathVariable("petId") Long petId, @PathVariable("dailyPackagedSnackId") Long dailyPackagedSnackId) {
        ReadDailyPackagedSnackResponseDto dailyPackagedSnack =
                dailyPackagedSnackService.getDailyPackagedSnack(username, petId, dailyPackagedSnackId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.createSuccess(dailyPackagedSnack));
    }

    @Operation(summary = "섭취 포장 간식 제거")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "포장 간식 성공적 제거"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "본인의 반려견이 아닌 경우, 해당 반려견의 포장 간식이 아닌 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "잘못된 petId, 잘못된 dailyPackagedSnackId")
    })
    @DeleteMapping("pet/{petId}/packagedSnacks/{dailyPackagedSnackId}")
    public ResponseEntity<BaseResponse> deleteDailyFeed(@CurrentUserUsername String username, @PathVariable("petId") Long petId, @PathVariable("dailyPackagedSnackId") Long dailyPackagedSnackId) {
        dailyPackagedSnackService.deleteDailyPackagedSnack(username,petId, dailyPackagedSnackId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.createSuccess(null));
    }

    @Operation(summary = "특정 식사에서 섭취한 포장 간식들 제거")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "포장 간식 성공적 제거"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "본인의 반려견이 아닌 경우, 해당 반려견의 식사 내역이 아닌 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "잘못된 petId, 잘못된 dailyMealId")
    })
    @DeleteMapping("/pet/{petId}/dailyMeals/{dailyMealId}/packagedSnacks")
    public ResponseEntity<BaseResponse> deleteDailyPackagedSnacks(@CurrentUserUsername String username, @PathVariable("petId") Long petId, @PathVariable("dailyMealId") Long dailyMealId) {
        dailyPackagedSnackService.deleteDailyPackagedSnacks(username, petId, dailyMealId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.createSuccessWithNoContent());
    }
}
