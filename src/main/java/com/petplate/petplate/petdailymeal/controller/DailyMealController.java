package com.petplate.petplate.petdailymeal.controller;

import com.petplate.petplate.auth.interfaces.CurrentUserUsername;
import com.petplate.petplate.common.response.BaseResponse;
import com.petplate.petplate.dailyMealNutrient.repository.ProperNutrientRepository;
import com.petplate.petplate.dailyMealNutrient.service.DeficientNutrientService;
import com.petplate.petplate.dailyMealNutrient.service.ProperNutrientService;
import com.petplate.petplate.dailyMealNutrient.service.SufficientNutrientService;
import com.petplate.petplate.pet.dto.response.ReadPetNutrientResponseDto;
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
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "식사(섭취내역) 컨트롤러", description = "반려견의 식사에 대한 컨트롤러 입니다")
public class DailyMealController {
    private final DailyMealService dailyMealService;
    private final DeficientNutrientService deficientNutrientService;
    private final SufficientNutrientService sufficientNutrientService;
    private final ProperNutrientService properNutrientService;

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
    // 수정
    @GetMapping("/pet/{petId}/dailyMeals")
    public ResponseEntity<BaseResponse<List<ReadDailyMealResponseDto>>> readDailyMealsWithAllDailyFoods(@CurrentUserUsername String username, @PathVariable("petId") Long petId,
                                                                                                        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        if (date == null) {
            List<ReadDailyMealResponseDto> dailyMealsWithFoods = dailyMealService.getDailyMealsWithAllFoods(username, petId);
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.createSuccess(dailyMealsWithFoods));
        } else {
            List<ReadDailyMealResponseDto> responseDtos = new ArrayList<>();
            ReadDailyMealResponseDto dailyMealWithFoods = dailyMealService.getDailyMealWithAllFoods(username, petId, date);
            responseDtos.add(dailyMealWithFoods);
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.createSuccess(responseDtos));
        }
    }

    @Operation(summary = "반려견의 식사 내역 중 자연식만 조회", description = "특정일자의 식사 내역 중 자연식(dailyRaws)만 조회합니다. (나머진 null)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "식사내역 성공적 조회"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "조회하려는 반려견이 본인의 반려견이 아닌 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "잘못된 petId, 해당 일자에 식사 내역이 없는 경우"),
    })
    @GetMapping("/pet/{petId}/dailyMeals/raws")
    public ResponseEntity<BaseResponse<ReadDailyMealResponseDto>> readDailyMealsWithDailyRaws(@CurrentUserUsername String username, @PathVariable("petId") Long petId,
                                                                                              @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        ReadDailyMealResponseDto dailyMealWithFoods = dailyMealService.getDailyMealWithDailyRaws(username, petId, date);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.createSuccess(dailyMealWithFoods));
    }

    @Operation(summary = "반려견의 식사 내역 중 사료만 조회", description = "특정일자의 식사 내역 중 사료(dailyFeeds)만 조회합니다. (나머진 null)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "식사내역 성공적 조회"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "조회하려는 반려견이 본인의 반려견이 아닌 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "잘못된 petId, 해당 일자에 식사 내역이 없는 경우"),
    })
    @GetMapping("/pet/{petId}/dailyMeals/feeds")
    public ResponseEntity<BaseResponse<ReadDailyMealResponseDto>> readDailyMealsWithDailyFeeds(@CurrentUserUsername String username, @PathVariable("petId") Long petId,
                                                                                               @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        ReadDailyMealResponseDto dailyMealWithFoods = dailyMealService.getDailyMealWithDailyFeeds(username, petId, date);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.createSuccess(dailyMealWithFoods));
    }

    @Operation(summary = "반려견의 식사 내역 중 포장 간식만 조회", description = "특정일자의 식사 내역 중 포장 간식(dailyPackagedSnacks)만 조회합니다. (나머진 null)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "식사내역 성공적 조회"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "조회하려는 반려견이 본인의 반려견이 아닌 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "잘못된 petId, 해당 일자에 식사 내역이 없는 경우"),
    })
    @GetMapping("/pet/{petId}/dailyMeals/packagedSnacks")
    public ResponseEntity<BaseResponse<ReadDailyMealResponseDto>> readDailyMealsWithDailyPackagedSnacks(@CurrentUserUsername String username, @PathVariable("petId") Long petId,
                                                                                                        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        ReadDailyMealResponseDto dailyMealWithFoods = dailyMealService.getDailyMealWithDailyPackagedSnacks(username, petId, date);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.createSuccess(dailyMealWithFoods));
    }

    @Operation(summary = "반려견의 식사 내역 중 즐겨찾기 자연식만 조회", description = "특정일자의 식사 내역 중 즐겨찾기 자연식(dailyBookMarkedRaws)만 조회합니다. (나머진 null)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "식사내역 성공적 조회"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "조회하려는 반려견이 본인의 반려견이 아닌 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "잘못된 petId, 해당 일자에 식사 내역이 없는 경우"),
    })
    @GetMapping("/pet/{petId}/dailyMeals/bookmark/raws")
    public ResponseEntity<BaseResponse<ReadDailyMealResponseDto>> readDailyMealsWithDailyBookMarkedRaws(@CurrentUserUsername String username, @PathVariable("petId") Long petId,
                                                                                                        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        ReadDailyMealResponseDto dailyMealWithFoods = dailyMealService.getDailyMealWithDailyBookMarkedRaws(username, petId, date);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.createSuccess(dailyMealWithFoods));
    }

    @Operation(summary = "반려견의 식사 내역 중 즐겨찾기 사료만 조회", description = "특정일자의 식사 내역 중 즐겨찾기 사료(dailyBookMarkedFeeds)만 조회합니다. (나머진 null)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "식사내역 성공적 조회"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "조회하려는 반려견이 본인의 반려견이 아닌 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "잘못된 petId, 해당 일자에 식사 내역이 없는 경우"),
    })
    @GetMapping("/pet/{petId}/dailyMeals/bookmark/feeds")
    public ResponseEntity<BaseResponse<ReadDailyMealResponseDto>> readDailyMealsWithDailyBookMarkedFeeds(@CurrentUserUsername String username, @PathVariable("petId") Long petId,
                                                                                                         @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        ReadDailyMealResponseDto dailyMealWithFoods = dailyMealService.getDailyMealWithDailyBookMarkedFeeds(username, petId, date);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.createSuccess(dailyMealWithFoods));
    }

    @Operation(summary = "반려견의 식사 내역 중 즐겨찾기 포장간식만 조회", description = "특정일자의 식사 내역 중 즐겨찾기 포장간식(dailyBookMarkedPackagedSnacks)만 조회합니다. (나머진 null)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "식사내역 성공적 조회"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "조회하려는 반려견이 본인의 반려견이 아닌 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "잘못된 petId, 해당 일자에 식사 내역이 없는 경우"),
    })
    @GetMapping("/pet/{petId}/dailyMeals/bookmark/packagedSnacks")
    public ResponseEntity<BaseResponse<ReadDailyMealResponseDto>> readDailyMealsWithDailyBookMarkedPackagedSnacks(@CurrentUserUsername String username, @PathVariable("petId") Long petId,
                                                                                                                  @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        ReadDailyMealResponseDto dailyMealWithFoods = dailyMealService.getDailyMealWithDailyBookMarkedPackagedSnacks(username, petId, date);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.createSuccess(dailyMealWithFoods));
    }

    @Operation(summary = "반려견의 오늘 식사 내역에 대한 영양분석을 진행(부족영양소, 과잉영양소, 적정영양소)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = CREATED, description = "영양 분석 성공(오늘 식사에 대한 부족/과잉/적정 영양소 내용을 성공적 저장)"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "조회하려는 반려견이 본인의 반려견이 아닌 경우, 이미 오늘 영양소 분석을 진행 한 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "잘못된 petId, 해당 일자에 식사 내역이 없는 경우"),
    })
    @PostMapping("/pet/{petId}/dailyMeals/nutrient")
    public ResponseEntity<BaseResponse> createDailyMealNutrients(@CurrentUserUsername String username, @PathVariable Long petId) {
        // 부족 영양소
        deficientNutrientService.createDeficientNutrientToday(username, petId);

        // 과잉 영양소
        sufficientNutrientService.createSufficientNutrientsToday(username, petId);

        // 적정 영양소
        properNutrientService.createProperNutrientsToday(username, petId);


        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.createSuccess(null));
    }

    @Operation(summary = "반려견의 식사 내역 중 부족 영양소를 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "영양소 성공적 조회"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "조회하려는 반려견이 본인의 반려견이 아닌 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "잘못된 petId, 해당 일자에 식사 내역이 없는 경우"),
    })
    @GetMapping("/pet/{petId}/dailyMeals/{dailyMealId}/nutrient/deficient")
    public ResponseEntity<BaseResponse<List<ReadPetNutrientResponseDto>>> readDeficientNutrients(@CurrentUserUsername String username, @PathVariable("petId") Long petId, @PathVariable Long dailyMealId) {
        List<ReadPetNutrientResponseDto> deficientNutrients = deficientNutrientService.getDeficientNutrients(username, petId, dailyMealId);

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.createSuccess(deficientNutrients));
    }

    @Operation(summary = "반려견의 식사 내역 중 과잉 영양소를 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "영양소 성공적 조회"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "조회하려는 반려견이 본인의 반려견이 아닌 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "잘못된 petId, 해당 일자에 식사 내역이 없는 경우"),
    })
    @GetMapping("/pet/{petId}/dailyMeals/{dailyMealId}/nutrient/sufficient")
    public ResponseEntity<BaseResponse<List<ReadPetNutrientResponseDto>>> readSufficientNutrients(@CurrentUserUsername String username, @PathVariable("petId") Long petId, @PathVariable Long dailyMealId) {
        List<ReadPetNutrientResponseDto> sufficientNutrients = sufficientNutrientService.getSufficientNutrients(username, petId, dailyMealId);

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.createSuccess(sufficientNutrients));
    }

    @Operation(summary = "반려견의 식사 내역 중 적정 영양소를 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "영양소 성공적 조회"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "조회하려는 반려견이 본인의 반려견이 아닌 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "잘못된 petId, 해당 일자에 식사 내역이 없는 경우"),
    })
    @GetMapping("/pet/{petId}/dailyMeals/{dailyMealId}/nutrient/proper")
    public ResponseEntity<BaseResponse<List<ReadPetNutrientResponseDto>>> readProperNutrients(@CurrentUserUsername String username, @PathVariable("petId") Long petId, @PathVariable Long dailyMealId) {
        List<ReadPetNutrientResponseDto> properNutrients = properNutrientService.getProperNutrients(username, petId, dailyMealId);

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.createSuccess(properNutrients));
    }
}
