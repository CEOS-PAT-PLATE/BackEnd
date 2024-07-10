package com.petplate.petplate.petfood.controller;

import com.petplate.petplate.auth.interfaces.CurrentUserUsername;
import com.petplate.petplate.common.response.BaseResponse;
import com.petplate.petplate.petdailymeal.dto.request.CreateDailyBookMarkedFeedRequestDto;
import com.petplate.petplate.petdailymeal.dto.request.CreateDailyBookMarkedRawRequestDto;
import com.petplate.petplate.petdailymeal.service.DailyBookMarkedFeedService;
import com.petplate.petplate.petfood.dto.request.CreateBookMarkedFeedRequestDto;
import com.petplate.petplate.petfood.dto.response.ReadBookMarkedFeedResponseDto;
import com.petplate.petplate.petfood.service.BookMarkedFeedService;
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
@Tag(name = "즐겨찾기한 사료 컨트롤러", description = "사료(즐겨찾기) 및 펫이 하룻 동안 섭취한 사료(즐겨찾기)에 대한 컨트롤러 입니다")
public class BookMarkedFeedController {
    private final BookMarkedFeedService bookMarkedFeedService;
    private final DailyBookMarkedFeedService dailyBookMarkedFeedService;

    private static final String OK = "200";
    private static final String CREATED = "201";
    private static final String BAD_REQUEST = "400";
    private static final String NOT_FOUND = "404";

    @Operation(summary = "즐겨찾기에 사료를 추가")
    @ApiResponses(value = {
            @ApiResponse(responseCode = CREATED, description = "즐겨찾기 성공적 추가"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "해당 유저의 즐겨찾기에 이미 중복되는 이름과 섭취량의 즐겨찾기 사료가 존재하는 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "해당 username을 가지는 유저가 존재하지 않는 경우")
    })
    @PostMapping("/bookmark/feeds")
    public ResponseEntity<BaseResponse<Long>> createBookMarkedFeed(@CurrentUserUsername String username, @Valid @RequestBody CreateBookMarkedFeedRequestDto requestDto) {
        Long bookMarkedFeedId = bookMarkedFeedService.createBookMarkedFeed(username, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.createSuccess(bookMarkedFeedId));
    }

    @Operation(summary = "유저의 즐겨찾기한 사료 모두 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "즐겨찾기 사료 성공적 조회"),
    })
    @GetMapping("/bookmark/feeds")
    public ResponseEntity<BaseResponse<List<ReadBookMarkedFeedResponseDto>>> readBookMarkedFeeds(@CurrentUserUsername String username) {
        List<ReadBookMarkedFeedResponseDto> bookMarkedFeeds = bookMarkedFeedService.getBookMarkedFeeds(username);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.createSuccess(bookMarkedFeeds));
    }

    @Operation(summary = "유저의 즐겨찾기한 사료 단건 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "즐겨찾기 사료 성공적 조회"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "본인의 즐겨찾기가 아닌 즐겨찾기를 조회하는 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "존재하지 않는 bookMarkedFeedId"),
    })
    @GetMapping("/bookmark/feeds/{bookMarkedFeedId}")
    public ResponseEntity<BaseResponse<ReadBookMarkedFeedResponseDto>> readBookMarkedFeed(@CurrentUserUsername String username, @PathVariable Long bookMarkedFeedId) {
        ReadBookMarkedFeedResponseDto bookMarkedFeed =
                bookMarkedFeedService.getBookMarkedFeed(username, bookMarkedFeedId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.createSuccess(bookMarkedFeed));
    }

    @Operation(summary = "즐겨찾기한 사료 제거")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "즐겨찾기 사료 성공적 제거"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "본인의 즐겨찾기가 아닌 즐겨찾기를 제거하려는 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "존재하지 않는 bookMarkedFeedId"),
    })
    @DeleteMapping("/bookmark/feeds/{bookMarkedFeedId}")
    public ResponseEntity<BaseResponse> deleteBookMarkedRaw(@CurrentUserUsername String username, @PathVariable Long bookMarkedFeedId) {
        bookMarkedFeedService.deleteBookMarkedFeed(username, bookMarkedFeedId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.createSuccess(null));
    }

    @Operation(summary = "오늘 식사내역에 섭취한 즐겨찾기 사료 저장", description = "오늘 식사 내역에 즐겨찾기한 사료를 추가합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = CREATED, description = "즐겨찾기 자연식 하루식사에 성공적 저장"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "조회하려는 반려견이 본인의 반려견이 아닌 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "존재하지 않는 petId, 존재하지 않는 bookMarkedRawId"),
    })
    @PostMapping("/pet/{petId}/bookmark/feeds")
    public ResponseEntity<BaseResponse<Long>> createDailyBookMarkRaw(@CurrentUserUsername String username, @PathVariable Long petId, @Valid @RequestBody CreateDailyBookMarkedFeedRequestDto requestDto) {
        Long id = dailyBookMarkedFeedService.createDailyBookMarkedFeed(username, petId, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.createSuccess(id));
    }

    @Operation(summary = "식사내역에서 섭취한 즐겨찾기 사료 제거", description = "식사내역에 있는 즐겨찾기 사료의 섭취 내역을 제거합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "즐겨찾기 자연식 성공적 제거"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "조회하려는 반려견이 본인의 반려견이 아닌 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "존재하지 않는 petId, 존재하지 않는 dailyBookMarkedRawId"),
    })
    @DeleteMapping("/pet/{petId}/bookmark/feeds/{dailyBookMarkedFeedId}")
    public ResponseEntity<BaseResponse> deleteDailyBookMarkedRaw(@CurrentUserUsername String username, @PathVariable Long petId, @PathVariable Long dailyBookMarkedFeedId) {
        dailyBookMarkedFeedService.deleteDailyBookMarkedFeed(username, petId, dailyBookMarkedFeedId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.createSuccess(null));
    }
}
