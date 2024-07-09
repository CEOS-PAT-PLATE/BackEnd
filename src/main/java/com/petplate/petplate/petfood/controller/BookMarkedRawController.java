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
            @ApiResponse(responseCode = NOT_FOUND, description = "해당 username을 가지는 유저가 존재하지 않는 경우, 존재하지 않는 rawId")
    })
    @PostMapping("/bookmark/raws")
    public ResponseEntity<BaseResponse<Long>> createBookMarkedRaws(@CurrentUserUsername String username, @Valid CreateBookMarkedRawRequestDto requestDto) {
        Long bookMarkedRawId = bookMarkedRawService.createBookMarkedRaw(username, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.createSuccess(bookMarkedRawId));
    }

    @Operation(summary = "유저의 즐겨찾기한 자연식 모두 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "즐겨찾기 자연식 성공적 조회"),
    })
    @GetMapping("/bookmark/raws")
    public ResponseEntity<BaseResponse<List<ReadBookMarkedRawResponseDto>>> readBookMarkedRaws(@CurrentUserUsername String username) {
        List<ReadBookMarkedRawResponseDto> bookMarkedRaws = bookMarkedRawService.getBookMarkedRaws(username);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.createSuccess(bookMarkedRaws));
    }

    @Operation(summary = "유저의 즐겨찾기한 자연식 단건 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "즐겨찾기 자연식 성공적 조회"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "본인의 즐겨찾기가 아닌 즐겨찾기를 조회하는 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "존재하지 않는 bookMarkedRawId"),
    })
    @GetMapping("/bookmark/raws/{bookMarkedRawId}")
    public ResponseEntity<BaseResponse<ReadBookMarkedRawResponseDto>> readBookMarkedRaw(@CurrentUserUsername String username, @PathVariable Long bookMarkedRawId) {
        ReadBookMarkedRawResponseDto bookMarkedRaw = bookMarkedRawService.getBookMarkedRaw(username, bookMarkedRawId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.createSuccess(bookMarkedRaw));
    }

    @Operation(summary = "즐겨찾기한 자연식 제거")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "즐겨찾기 자연식 성공적 제거"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "본인의 즐겨찾기가 아닌 즐겨찾기를 제거하려는 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "존재하지 않는 bookMarkedRawId"),
    })
    @DeleteMapping("/bookmark/raws/{bookMarkedRawId}")
    public ResponseEntity<BaseResponse> deleteBookMarkedRaw(@CurrentUserUsername String username, @PathVariable Long bookMarkedRawId) {
        bookMarkedRawService.deleteBookMarkedRaw(username, bookMarkedRawId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.createSuccess(null));
    }

    @Operation(summary = "오늘 식사내역에 섭취한 즐겨찾기 자연식 저장", description = "오늘 식사 내역에 즐겨찾기한 자연식을 추가합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = CREATED, description = "즐겨찾기 자연식 하루식사에 성공적 저장"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "조회하려는 반려견이 본인의 반려견이 아닌 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "존재하지 않는 petId, 존재하지 않는 bookMarkedRawId"),
    })
    @PostMapping("/pet/{petId}/bookmark/raws")
    public ResponseEntity<BaseResponse<Long>> createDailyBookMarkRaw(@CurrentUserUsername String username, @PathVariable Long petId, @Valid CreateDailyBookMarkedRawRequestDto requestDto) {
        Long id = dailyBookMarkedRawService.createDailyBookMarkedRaw(username, petId, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.createSuccess(id));
    }

    @Operation(summary = "오늘 식사내역에서 섭취한 즐겨찾기 자연식 제거", description = "오늘 식사내역에 있는 즐겨찾기 자연식의 섭취 내역을 제거합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "즐겨찾기 자연식 성공적 제거"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "조회하려는 반려견이 본인의 반려견이 아닌 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "존재하지 않는 petId, 존재하지 않는 dailyBookMarkedRawId"),
    })
    @DeleteMapping("/pet/{petId}/bookmark/raws/{dailyBookMarkedRawId}")
    public ResponseEntity<BaseResponse> deleteDailyBookMarkedRaw(@CurrentUserUsername String username, @PathVariable Long petId, @PathVariable Long dailyBookMarkedRawId) {
        dailyBookMarkedRawService.deleteDailyBookMarkedRaw(username, petId, dailyBookMarkedRawId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.createSuccess(null));
    }
}
