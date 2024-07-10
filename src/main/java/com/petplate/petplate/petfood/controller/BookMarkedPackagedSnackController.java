package com.petplate.petplate.petfood.controller;

import com.petplate.petplate.auth.interfaces.CurrentUserUsername;
import com.petplate.petplate.common.response.BaseResponse;
import com.petplate.petplate.petdailymeal.dto.request.CreateDailyBookMarkedPackagedSnackRequestDto;
import com.petplate.petplate.petdailymeal.service.DailyBookMarkedPackagedSnackService;
import com.petplate.petplate.petfood.dto.request.CreateBookMarkedPackagedSnackRequestDto;
import com.petplate.petplate.petfood.dto.response.ReadBookMarkedPackagedSnackResponseDto;
import com.petplate.petplate.petfood.service.BookMarkedPackagedSnackService;
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
@Tag(name = "즐겨찾기한 포장간식 컨트롤러", description = "포장간식(즐겨찾기) 및 펫이 하룻 동안 섭취한 포장간식(즐겨찾기)에 대한 컨트롤러 입니다")
public class BookMarkedPackagedSnackController {
    private final BookMarkedPackagedSnackService bookMarkedPackagedSnackService;
    private final DailyBookMarkedPackagedSnackService dailyBookMarkedPackagedSnackService;

    private static final String OK = "200";
    private static final String CREATED = "201";
    private static final String BAD_REQUEST = "400";
    private static final String NOT_FOUND = "404";

    @Operation(summary = "즐겨찾기에 포장간식을 추가")
    @ApiResponses(value = {
            @ApiResponse(responseCode = CREATED, description = "즐겨찾기 성공적 추가"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "해당 유저의 즐겨찾기에 이미 중복되는 이름과 섭취량의 즐겨찾기 포장간식이 존재하는 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "해당 username을 가지는 유저가 존재하지 않는 경우")
    })
    @PostMapping("/bookmark/PackagedSnacks")
    public ResponseEntity<BaseResponse<Long>> createBookMarkedPackagedSnack(@CurrentUserUsername String username, @Valid @RequestBody CreateBookMarkedPackagedSnackRequestDto requestDto) {
        Long bookMarkedPackagedSnackId = bookMarkedPackagedSnackService.createBookMarkedPackagedSnack(username, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.createSuccess(bookMarkedPackagedSnackId));
    }

    @Operation(summary = "유저의 즐겨찾기한 포장간식 모두 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "즐겨찾기 포장간식 성공적 조회"),
    })
    @GetMapping("/bookmark/PackagedSnacks")
    public ResponseEntity<BaseResponse<List<ReadBookMarkedPackagedSnackResponseDto>>> readBookMarkedPackagedSnacks(@CurrentUserUsername String username) {
        List<ReadBookMarkedPackagedSnackResponseDto> bookMarkedPackagedSnacks = bookMarkedPackagedSnackService.getBookMarkedPackagedSnacks(username);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.createSuccess(bookMarkedPackagedSnacks));
    }

    @Operation(summary = "유저의 즐겨찾기한 포장간식 단건 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "즐겨찾기 포장간식 성공적 조회"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "본인의 즐겨찾기가 아닌 즐겨찾기를 조회하는 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "존재하지 않는 bookMarkedPackagedSnackId"),
    })
    @GetMapping("/bookmark/PackagedSnacks/{bookMarkedPackagedSnackId}")
    public ResponseEntity<BaseResponse<ReadBookMarkedPackagedSnackResponseDto>> readBookMarkedPackagedSnack(@CurrentUserUsername String username, @PathVariable Long bookMarkedPackagedSnackId) {
        ReadBookMarkedPackagedSnackResponseDto bookMarkedPackagedSnack =
                bookMarkedPackagedSnackService.getBookMarkedPackagedSnack(username, bookMarkedPackagedSnackId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.createSuccess(bookMarkedPackagedSnack));
    }

    @Operation(summary = "즐겨찾기한 포장간식 제거")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "즐겨찾기 포장간식 성공적 제거"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "본인의 즐겨찾기가 아닌 즐겨찾기를 제거하려는 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "존재하지 않는 bookMarkedPackagedSnackId"),
    })
    @DeleteMapping("/bookmark/PackagedSnacks/{bookMarkedPackagedSnackId}")
    public ResponseEntity<BaseResponse> deleteBookMarkedRaw(@CurrentUserUsername String username, @PathVariable Long bookMarkedPackagedSnackId) {
        bookMarkedPackagedSnackService.deleteBookMarkedPackagedSnack(username, bookMarkedPackagedSnackId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.createSuccess(null));
    }

    @Operation(summary = "오늘 식사내역에 섭취한 즐겨찾기 포장간식 저장", description = "오늘 식사 내역에 즐겨찾기한 포장간식을 추가합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = CREATED, description = "즐겨찾기 자연식 하루식사에 성공적 저장"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "조회하려는 반려견이 본인의 반려견이 아닌 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "존재하지 않는 petId, 존재하지 않는 bookMarkedRawId"),
    })
    @PostMapping("/pet/{petId}/bookmark/PackagedSnacks")
    public ResponseEntity<BaseResponse<Long>> createDailyBookMarkRaw(@CurrentUserUsername String username, @PathVariable Long petId, @Valid @RequestBody CreateDailyBookMarkedPackagedSnackRequestDto requestDto) {
        Long id = dailyBookMarkedPackagedSnackService.createDailyBookMarkedPackagedSnack(username, petId, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.createSuccess(id));
    }

    @Operation(summary = "식사내역에서 섭취한 즐겨찾기 포장간식 제거", description = "식사내역에 있는 즐겨찾기 포장간식의 섭취 내역을 제거합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "즐겨찾기 자연식 성공적 제거"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "조회하려는 반려견이 본인의 반려견이 아닌 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "존재하지 않는 petId, 존재하지 않는 dailyBookMarkedRawId"),
    })
    @DeleteMapping("/pet/{petId}/bookmark/PackagedSnacks/{dailyBookMarkedPackagedSnackId}")
    public ResponseEntity<BaseResponse> deleteDailyBookMarkedRaw(@CurrentUserUsername String username, @PathVariable Long petId, @PathVariable Long dailyBookMarkedPackagedSnackId) {
        dailyBookMarkedPackagedSnackService.deleteDailyBookMarkedPackagedSnack(username, petId, dailyBookMarkedPackagedSnackId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.createSuccess(null));
    }
}
