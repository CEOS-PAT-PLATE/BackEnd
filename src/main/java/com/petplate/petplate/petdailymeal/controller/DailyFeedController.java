package com.petplate.petplate.petdailymeal.controller;

import com.petplate.petplate.auth.interfaces.CurrentUserUsername;
import com.petplate.petplate.common.response.BaseResponse;
import com.petplate.petplate.petdailymeal.dto.request.CreateDailyFeedRequestDto;
import com.petplate.petplate.petdailymeal.dto.response.ReadDailyFeedResponseDto;
import com.petplate.petplate.petdailymeal.service.DailyFeedService;
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
@Tag(name = "사료 컨트롤러", description = "반려견이 섭취한 사료에 대한 컨트롤러 입니다")
public class DailyFeedController {
    private final DailyFeedService dailyFeedService;

    private static final String OK = "200";
    private static final String CREATED = "201";
    private static final String BAD_REQUEST = "400";
    private static final String NOT_FOUND = "404";

    @Operation(summary = "섭취 사료 저장", description = "오늘 식사내역에 해당 사료의 정보를 추가합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = CREATED, description = "사료 성공적 추가"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "본인의 반려견이 아닌 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "잘못된 petId")
    })
    @PostMapping("pet/{petId}/feeds")
    public ResponseEntity<BaseResponse<Long>> createDailyFeed(@CurrentUserUsername String username, @PathVariable Long petId, @Valid CreateDailyFeedRequestDto requestDto) {
        Long id = dailyFeedService.createDailyFeed(username, petId, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.createSuccess(id));
    }

    @Operation(summary = "섭취 사료 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = CREATED, description = "사료 성공적 조회"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "본인의 반려견이 아닌 경우, 해당 반려견의 사료가 아닌 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "잘못된 petId, 잘못된 dailyFeedId")
    })
    @GetMapping("pet/{petId}/feeds/{dailyFeedId}")
    public ResponseEntity<BaseResponse<ReadDailyFeedResponseDto>> readDailyFeed(@CurrentUserUsername String username, @PathVariable Long petId, @PathVariable Long dailyFeedId) {
        ReadDailyFeedResponseDto dailyFeed = dailyFeedService.getDailyFeed(username, petId, dailyFeedId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.createSuccess(dailyFeed));
    }

     @Operation(summary = "섭취 사료 제거")
    @ApiResponses(value = {
            @ApiResponse(responseCode = CREATED, description = "사료 성공적 제거"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "본인의 반려견이 아닌 경우, 해당 반려견의 사료가 아닌 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "잘못된 petId, 잘못된 dailyFeedId")
    })
    @DeleteMapping("pet/{petId}/feeds/{dailyFeedId}")
    public ResponseEntity<BaseResponse> deleteDailyFeed(@CurrentUserUsername String username, @PathVariable Long petId, @PathVariable Long dailyFeedId) {
        dailyFeedService.deleteDailyFeed(username,petId, dailyFeedId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.createSuccess(null));
    }
}
