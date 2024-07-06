package com.petplate.petplate.petfood.controller;

import com.petplate.petplate.auth.interfaces.CurrentUserUsername;
import com.petplate.petplate.common.response.BaseResponse;
import com.petplate.petplate.petdailymeal.dto.request.CreateDailyBookMarkedRawRequestDto;
import com.petplate.petplate.petdailymeal.dto.response.ReadDailyBookMarkedRawResponseDto;
import com.petplate.petplate.petdailymeal.service.DailyBookMarkedRawService;
import com.petplate.petplate.petfood.dto.request.CreateBookMarkedRawRequestDto;
import com.petplate.petplate.petfood.dto.response.ReadBookMarkedRawResponseDto;
import com.petplate.petplate.petfood.service.BookMarkedRawService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "즐겨찾기한 자연식 컨트롤러", description = "자연식(즐겨찾기) 및 펫이 하룻 동안 섭취한 자연식(즐겨찾기)에 대한 컨트롤러 입니다")
public class BookMarkedRawController {

    private final DailyBookMarkedRawService dailyBookMarkedRawService;
    private final BookMarkedRawService bookMarkedRawService;

    private static final String OK = "200";
    private static final String CREATED = "201";
    private static final String BAD_REQUEST = "400";
    private static final String NOT_FOUND = "404";


    @Operation(summary = "자연식을 즐겨찾기에 추가")
    @ApiResponses(value = {
            @ApiResponse(responseCode = CREATED, description = "즐겨찾기 성공적 추가"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "해당 유저의 즐겨찾기에 이미 동일한 식품 + 동일 섭취량을 가진 즐겨찾기 정보가 존재하는 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "해당 username을 가지는 유저가 존재하지 않는 경우, 해당 rawId를 가지는 자연식이 존재하지 않는 경우")
    })
    @PostMapping("/bookmark/raws")
    public ResponseEntity<BaseResponse<Long>> createBookMarkedRaws(@CurrentUserUsername String username, @Valid CreateBookMarkedRawRequestDto requestDto) {
        Long bookMarkedRawId = bookMarkedRawService.createBookMarkedRaw(username, requestDto);

        return new ResponseEntity(BaseResponse.createSuccess(bookMarkedRawId), HttpStatus.CREATED);
    }

    @Operation(summary = "유저의 모든 즐겨찾기한 자연식 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "즐겨찾기 자연식 성공적 조회"),
    })
    @GetMapping("/bookmark/raws")
    public ResponseEntity<BaseResponse<List<ReadBookMarkedRawResponseDto>>> readBookMarkedRaws(@CurrentUserUsername String username) {
        List<ReadBookMarkedRawResponseDto> bookMarkedRaws = bookMarkedRawService.getBookMarkedRaws(username);

        return new ResponseEntity(BaseResponse.createSuccess(bookMarkedRaws), HttpStatus.OK);
    }

    @Operation(summary = "특정 즐겨찾기한 자연식 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "즐겨찾기 자연식 성공적 조회"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "본인의 즐겨찾기가 아닌 즐겨찾기를 조회하는 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "존재하지 않는 즐겨찾기를 조회하는 경우"),
    })
    @GetMapping("/bookmark/raws/{bookMarkedRawId}")
    public ResponseEntity<BaseResponse<ReadBookMarkedRawResponseDto>> readBookMarkedRaw(@CurrentUserUsername String username, @PathVariable Long bookMarkedRawId) {
        ReadBookMarkedRawResponseDto bookMarkedRaw = bookMarkedRawService.getBookMarkedRaw(username, bookMarkedRawId);

        return new ResponseEntity(BaseResponse.createSuccess(bookMarkedRaw), HttpStatus.OK);
    }

    @Operation(summary = "즐겨찾기한 자연식 제거")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "즐겨찾기 자연식 성공적 제거"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "본인의 즐겨찾기가 아닌 즐겨찾기를 제거하려는 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "존재하지 않는 즐겨찾기를 제거하려는 경우"),
    })
    @DeleteMapping("/bookmark/raws/{bookMarkedRawId}")
    public ResponseEntity<BaseResponse> deleteBookMarkedRaw(@CurrentUserUsername String username, @PathVariable Long bookMarkedRawId) {
        bookMarkedRawService.deleteBookMarkedRaw(username, bookMarkedRawId);

        return new ResponseEntity(BaseResponse.createSuccess(null), HttpStatus.OK);
    }

    @Operation(summary = "섭취한 즐겨찾기 자연식 저장", description = "오늘 식사 내역에 즐겨찾기한 저장식의 섭취 내역을 저장합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "즐겨찾기 자연식 하루식사에 성공적 저장"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "조회하려는 반려견이 본인의 반려견이 아닌 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "잘못된 petId, 잘못된 bookMarkedRawId"),
    })
    @PostMapping("/pet/{petId}/bookmark/raws")
    public ResponseEntity<BaseResponse<Long>> createDailyBookMarkRaw(@CurrentUserUsername String username, @PathVariable Long petId, @Valid CreateDailyBookMarkedRawRequestDto requestDto) {
        Long id = dailyBookMarkedRawService.createDailyBookMarkedRaw(username, petId, requestDto);

        return new ResponseEntity(BaseResponse.createSuccess(id), HttpStatus.CREATED);
    }

    @Operation(summary = "섭취한 즐겨찾기 자연식 제거", description = "즐겨찾기 자연식의 섭취 내역을 제거합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "즐겨찾기 자연식 성공적 제거"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "조회하려는 반려견이 본인의 반려견이 아닌 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "잘못된 petId, 잘못된 dailyBookMarkedRawId"),
    })
    @DeleteMapping("/pet/{petId}/bookmark/raws/{dailyBookMarkedRawId}")
    public ResponseEntity<BaseResponse> deleteDailyBookMarkedRaw(@CurrentUserUsername String username, @PathVariable Long petId, @PathVariable Long dailyBookMarkedRawId) {
        dailyBookMarkedRawService.deleteDailyBookMarkedRaw(username, petId, dailyBookMarkedRawId);

        return new ResponseEntity(BaseResponse.createSuccess(null), HttpStatus.OK);
    }
}
