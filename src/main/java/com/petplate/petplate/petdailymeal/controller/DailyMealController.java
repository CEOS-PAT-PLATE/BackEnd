package com.petplate.petplate.petdailymeal.controller;

import com.petplate.petplate.auth.interfaces.CurrentUserUsername;
import com.petplate.petplate.common.response.BaseResponse;
import com.petplate.petplate.petdailymeal.dto.response.ReadDailyMealResponseDto;
import com.petplate.petplate.petdailymeal.service.DailyMealService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "식사(섭취내역) 컨트롤러", description = "반려견의 식사에 대한 컨트롤러 입니다")
public class DailyMealController {
    private final DailyMealService dailyMealService;

    private static final String OK = "200";
    private static final String CREATED = "201";
    private static final String BAD_REQUEST = "400";
    private static final String NOT_FOUND = "404";

    @Operation(summary = "반려견의 식사 내역 조회", description = "특정일자의 식사 내역을 조회합니다. (날짜 미입력시 모든 식사 내역 조회). 반환 값으로는 식사의 PK, 식사 날짜 그리고 해당 식사에 포함된 자연식, 사료, 포장간식, 즐겨찾기한 자연식, 즐겨찾기한 사료, 즐겨찾기한 포장간식의 정보가 포함됩니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "식사내역 성공적 조회"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "조회하려는 반려견이 본인의 반려견이 아닌 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "잘못된 petId, 해당 일자에 식사 내역이 없는 경우"),
    })
    @GetMapping("/pet/{petId}/dailyMeals")
    public ResponseEntity<BaseResponse<List<ReadDailyMealResponseDto>>> readDailyMeals(@CurrentUserUsername String username, @PathVariable("petId") Long petId,
                                                                                       @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        if (date == null) {
            List<ReadDailyMealResponseDto> dailyMealsWithFoods = dailyMealService.getDailyMealsWithFoods(username, petId);
            return new ResponseEntity(BaseResponse.createSuccess(dailyMealsWithFoods), HttpStatus.OK);
        } else{
            ReadDailyMealResponseDto dailyMealWithFoods = dailyMealService.getDailyMealWithFoods(username, petId, date);
            return new ResponseEntity(BaseResponse.createSuccess(dailyMealWithFoods), HttpStatus.OK);
        }
    }

}
