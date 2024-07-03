package com.petplate.petplate.petfood.controller;

import com.petplate.petplate.auth.interfaces.CurrentUserUsername;
import com.petplate.petplate.common.response.BaseResponse;
import com.petplate.petplate.petdailymeal.domain.entity.DailyMeal;
import com.petplate.petplate.petdailymeal.dto.request.CreateDailyRawRequestDto;
import com.petplate.petplate.petdailymeal.dto.response.ReadDailyRawResponseDto;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "자연식 컨트롤러", description = "자연식 및 펫이 하룻 동안 섭취한 자연식에 대한 컨트롤러 입니다")
public class RawController {
    private final RawService rawService;
    private final DailyMealService dailyMealService;
    private final DailyRawService dailyRawService;


    @Operation(summary = "자연식 추가")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "자연식 성공적 추가"),
            @ApiResponse(responseCode = "400", description = "동명의 자연식이 존재")
    })
    @PostMapping("/raws")
    public ResponseEntity<BaseResponse> createRaw(@Valid CreateRawRequestDto requestDto) {
        Long rawId = rawService.createRaw(requestDto);

        return new ResponseEntity<>(BaseResponse.createSuccess(rawId), HttpStatus.OK);
    }

    @Operation(summary = "rawId로 자연식 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "자연식 성공적 조회"),
            @ApiResponse(responseCode = "404", description = "존재하지 않은 자연식")
    })
    @GetMapping("/raws/{rawId}")
    public ResponseEntity<BaseResponse> readRaw(@PathVariable Long rawId) {
        ReadRawResponseDto response = rawService.getRaw(rawId);

        return new ResponseEntity<>(BaseResponse.createSuccess(response), HttpStatus.OK);
    }

    @Operation(summary = "keyword가 포함된 자연식 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "자연식 성공적 조회")
    })
    @GetMapping("/raws")
    public ResponseEntity<BaseResponse> readRawByKeyword(@RequestParam String keyword) {
        List<ReadRawResponseDto> responses = rawService.getRawByKeyword(keyword);

        return new ResponseEntity<>(BaseResponse.createSuccess(responses), HttpStatus.OK);
    }

    @Operation(summary = "rawId로 자연식 제거",description = "자연식이 제거된 경우, 해당 자연식을 참조하고 있던 즐겨찾기 자연식과 하루식사 자연식은 '존재하지 않은 정보입니다'로 표기됨")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "자연식 성공적 조회"),
            @ApiResponse(responseCode = "404", description = "존재하지 않은 자연식")
    })
    @DeleteMapping("/raws")
    public ResponseEntity<BaseResponse> deleteRaw(@RequestParam Long rawId) {
        rawService.deleteRawById(rawId);

        return new ResponseEntity<>(BaseResponse.createSuccess(null), HttpStatus.OK);
    }

    @Operation(summary = "섭취 자연식 저장")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",description = "섭취 자연식 성공적 저장"),
            @ApiResponse(responseCode = "404",description = "자연식 ID(rawId)가 제대로 입력되지 않음")
    })
    @PostMapping("/pets/{petId}/dailyMeals/raws")
    public ResponseEntity<BaseResponse> createDailyRaw(@CurrentUserUsername String username, @PathVariable Long petId, @Valid @RequestBody CreateDailyRawRequestDto requestDto) {
        Long dailyRawId = dailyRawService.createDailyRaw(username, petId, requestDto);

        return new ResponseEntity(BaseResponse.createSuccess(dailyRawId), HttpStatus.CREATED);
    }

    @Operation(summary = "특정 일자에 섭취한 자연식들 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "섭취 자연식 성공적 조회"),
            @ApiResponse(responseCode = "400",description = "유저의 반려견이 아님"),
            @ApiResponse(responseCode = "404",description = "존재하지 않은 반려견 혹은 존재하지 않은 식사 내역")
    })
    @GetMapping("/pets/{petId}/dailyMeals/raws")
    public ResponseEntity<BaseResponse> getDailyRaws(@CurrentUserUsername String username, @PathVariable Long petId, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        DailyMeal dailyMeal = dailyMealService.getDailyMeal(username, petId, date);
        List<ReadDailyRawResponseDto> dailyRaws = dailyRawService.getDailyRaws(username, petId, dailyMeal.getId());

        return new ResponseEntity<>(BaseResponse.createSuccess(dailyRaws), HttpStatus.OK);
    }

    @Operation(summary = "최근 2일 동안 섭취한 자연식들 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "섭취 자연식 성공적 조회"),
            @ApiResponse(responseCode = "400",description = "유저의 반려견이 아님"),
            @ApiResponse(responseCode = "404",description = "존재하지 않은 반려견")
    })
    @GetMapping("/pets/{petId}/dailyMeals/raws/recent")
    public ResponseEntity<BaseResponse> getRecentDailyRaws(@CurrentUserUsername String username, @PathVariable Long petId) {
        int days = 2;
        List<ReadDailyRawResponseDto> recentDailyRaws = dailyRawService.getRecentDailyRaws(username, petId, days);

        return new ResponseEntity<>(BaseResponse.createSuccess(recentDailyRaws), HttpStatus.OK);
    }

    @Operation(summary = "특정 섭취 자연식 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "섭취 자연식 성공적 조회"),
            @ApiResponse(responseCode = "400",description = "유저의 반려견이 아님"),
            @ApiResponse(responseCode = "404",description = "존재하지 않은 반려견 혹은 존재하지 않은 식사 내역")
    })
    @GetMapping("/pets/{petId}/dailyMeals/raws/{dailyRawId}")
    public ResponseEntity<BaseResponse> getDailyRaw(@CurrentUserUsername String username, @PathVariable Long petId, @PathVariable Long dailyRawId) {
        ReadDailyRawResponseDto dailyRaw = dailyRawService.getDailyRaw(username, petId, dailyRawId);

        return new ResponseEntity<>(BaseResponse.createSuccess(dailyRaw), HttpStatus.OK);
    }

    @Operation(summary = "특정 섭취 자연식 제거")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "섭취 자연식 성공적 조회"),
            @ApiResponse(responseCode = "400",description = "유저의 반려견이 아님"),
            @ApiResponse(responseCode = "404",description = "존재하지 않은 반려견 혹은 존재하지 않은 식사 내역")
    })
    @DeleteMapping("/pets/{petId}/dailyMeals/raws/{dailyRawId}")
    public ResponseEntity<BaseResponse> deleteDailyRaw(@CurrentUserUsername String username, @PathVariable Long petId, @PathVariable Long dailyRawId) {
        dailyRawService.deleteDailyRaw(username, petId, dailyRawId);

        return new ResponseEntity<>(BaseResponse.createSuccess(null), HttpStatus.OK);
    }
}
