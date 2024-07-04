package com.petplate.petplate.pet.controller;

import com.petplate.petplate.auth.interfaces.CurrentUserUsername;
import com.petplate.petplate.common.response.BaseResponse;
import com.petplate.petplate.pet.domain.entity.Pet;
import com.petplate.petplate.pet.dto.request.CreatePetRequestDto;
import com.petplate.petplate.pet.dto.request.ModifyPetInfoRequestDto;
import com.petplate.petplate.pet.dto.request.ModifyPetProfileImgRequestDto;
import com.petplate.petplate.pet.dto.response.*;
import com.petplate.petplate.pet.service.PetService;
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
@Tag(name = "반려견 컨트롤러", description = "반려견에 대한 컨트롤러 입니다")
public class PetController {

    private final PetService petService;
    private final String OK = "200";
    private final String CREATED = "201";
    private final String BAD_REQUEST = "400";
    private final String NOT_FOUND = "404";

    @Operation(summary = "반려견 추가")
    @ApiResponses(value = {
            @ApiResponse(responseCode = CREATED, description = "반려견 성공적 추가"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "멤버십이 존재하지 않으면서 두마리 이상의 반려견을 추가하려는 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "해당 username을 가지는 유저가 존재하지 않는 경우")
    })
    @PostMapping("/pets")
    public ResponseEntity<BaseResponse> createPet(@CurrentUserUsername String username, @RequestBody @Valid CreatePetRequestDto requestDto) {
        Pet pet = petService.createPet(username, requestDto);

        return new ResponseEntity(BaseResponse.createSuccess(pet.getName()), HttpStatus.CREATED);
    }

    @Operation(summary = "유저의 모든 반려견 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "반려견 성공적 조회"),
    })
    @GetMapping("/pets")
    public ResponseEntity<BaseResponse> readAllPets(@CurrentUserUsername String username) {
        List<ReadPetResponseDto> allPets = petService.getAllPets(username);

        return new ResponseEntity(BaseResponse.createSuccess(allPets), HttpStatus.OK);
    }

    @Operation(summary = "petId로 유저의 특정 반려견 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "반려견 성공적 조회"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "조회하려는 반려견이 본인의 반려견이 아닌 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "잘못된 petId"),
    })
    @GetMapping("/pets/{petId}")
    public ResponseEntity<BaseResponse> readAllPets(@CurrentUserUsername String username, @PathVariable Long petId) {
        ReadPetResponseDto pet = petService.getPet(username, petId);

        return new ResponseEntity(BaseResponse.createSuccess(pet), HttpStatus.OK);
    }

    @Operation(summary = "반려견 정보 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "반려견 성공적 정보 수정"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "조회하려는 반려견이 본인의 반려견이 아닌 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "잘못된 petId"),
    })
    @PutMapping("/pets/{petId}")
    public ResponseEntity<BaseResponse> modifyPet(@CurrentUserUsername String username, @PathVariable Long petId, @RequestBody @Valid ModifyPetInfoRequestDto requestDto) {
        petService.updatePetInfo(username, petId, requestDto);

        return new ResponseEntity(BaseResponse.createSuccess(null), HttpStatus.OK);
    }

    @Operation(summary = "선택할 수 있는 프로필 이미지들 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "프로필 이미지 성공적 조회"),
    })
    @GetMapping("/pets/images")
    public ResponseEntity<BaseResponse> readPetProfileImages() {
        List<ReadPetProfileImageResponseDto> petProfileImages = petService.getPetProfileImages();

        return new ResponseEntity(BaseResponse.createSuccess(petProfileImages), HttpStatus.OK);
    }

    @Operation(summary = "프로필 이미지 이름으로 선택한 프로필 이미지로 반려견 프로필 이미지 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "반려견 성공적 정보 수정"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "조회하려는 반려견이 본인의 반려견이 아닌 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "잘못된 petId"),
    })
    @PostMapping("/pets/{petId}/images")
    public ResponseEntity<BaseResponse> modifyPetProfileImage(@CurrentUserUsername String username, @PathVariable Long petId, @Valid ModifyPetProfileImgRequestDto requestDto) {
        petService.updateProfileImg(username, petId, requestDto);

        return new ResponseEntity(BaseResponse.createSuccess(null), HttpStatus.OK);
    }

    @Operation(summary = "반려견이 하루 섭취한 영양소 정보 조회. (날짜 미입력시 오늘 정보 조회)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "반려견 성공적 정보 조회"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "조회하려는 반려견이 본인의 반려견이 아닌 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "잘못된 petId, 해당 일자에 식사 내역이 없는 경우"),
    })
    @GetMapping("/pets/{petId}/nutrients")
    public ResponseEntity<BaseResponse> readPetNutrients(@CurrentUserUsername String username, @PathVariable Long petId,
                                                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        if (date == null) {
            List<ReadPetNutrientResponseDto> petNutrientToday = petService.getPetNutrientToday(username, petId);
            return new ResponseEntity(BaseResponse.createSuccess(petNutrientToday), HttpStatus.OK);
        } else {
            List<ReadPetNutrientResponseDto> petNutrient = petService.getPetNutrient(username, petId, date);
            return new ResponseEntity(BaseResponse.createSuccess(petNutrient), HttpStatus.OK);
        }
    }

    @Operation(summary = "반려견이 하루 섭취한 적정 영양소 대비 영양소 비율을 조회. (날짜 미입력시 오늘 정보 조회)",
            description = "반려견이 섭취한 영양소를 적정 섭취량에 대한 비율로 반환함\n" +
            "예) 체중에 대해서 계산한 단백질 적정량이 100g인데 총 200g을 섭취한 경우 protein = 2가 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "반려견 성공적 정보 조회"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "조회하려는 반려견이 본인의 반려견이 아닌 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "잘못된 petId, 해당 일자에 식사 내역이 없는 경우"),
    })
    @GetMapping("/pets/{petId}/nutrients/ratio")
    public ResponseEntity<BaseResponse> readPetNutrientRatio(@CurrentUserUsername String username, @PathVariable Long petId,
                                                             @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        if (date == null) {
            List<ReadPetNutrientRatioResponseDto> petNutrientRatioToday = petService.getPetNutrientRatioToday(username, petId);
            return new ResponseEntity(BaseResponse.createSuccess(petNutrientRatioToday), HttpStatus.OK);
        } else {
            List<ReadPetNutrientRatioResponseDto> petNutrientRatio = petService.getPetNutrientRatio(username, petId, date);
            return new ResponseEntity(BaseResponse.createSuccess(petNutrientRatio), HttpStatus.OK);
        }
    }

    @Operation(summary = "반려견이 하루동안 과잉 섭취한 영양소 조회. (날짜 미입력시 오늘 정보 조회)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "반려견 성공적 정보 조회"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "조회하려는 반려견이 본인의 반려견이 아닌 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "잘못된 petId, 해당 일자에 식사 내역이 없는 경우"),
    })
    @GetMapping("/pets/{petId}/nutrient/sufficient")
    public ResponseEntity<BaseResponse> readPetSufficientNutrients(@CurrentUserUsername String username, @PathVariable Long petId,
                                                                   @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        List<ReadPetNutrientResponseDto> sufficientNutrient = petService.getSufficientNutrient(username, petId, date);
        return new ResponseEntity(BaseResponse.createSuccess(sufficientNutrient), HttpStatus.OK);
    }

    @Operation(summary = "반려견이 하루동안 부족 섭취한 영양소 조회. (날짜 미입력시 오늘 정보 조회)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "반려견 성공적 정보 조회"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "조회하려는 반려견이 본인의 반려견이 아닌 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "잘못된 petId, 해당 일자에 식사 내역이 없는 경우"),
    })
    @GetMapping("/pets/{petId}/nutrient/deficient")
    public ResponseEntity<BaseResponse> readPetDeficientNutrients(@CurrentUserUsername String username, @PathVariable Long petId,
                                                                  @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        List<ReadPetNutrientResponseDto> deficientNutrient = petService.getDeficientNutrient(username, petId, date);
        return new ResponseEntity(BaseResponse.createSuccess(deficientNutrient), HttpStatus.OK);
    }

    @Operation(summary = "반려견이 하루동안 섭취한 칼로리 조회. (날짜 미입력시 오늘 정보 조회)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "반려견 성공적 정보 조회"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "조회하려는 반려견이 본인의 반려견이 아닌 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "잘못된 petId, 해당 일자에 식사 내역이 없는 경우"),
    })
    @GetMapping("/pets/{petId}/kcal")
    public ResponseEntity<BaseResponse> readPetKcal(@CurrentUserUsername String username, @PathVariable Long petId,
                                                    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        if (date == null) {
            ReadPetKcalResponseDto petKcalToday = petService.getPetKcalToday(username, petId);
            return new ResponseEntity(BaseResponse.createSuccess(petKcalToday), HttpStatus.OK);
        } else {
            ReadPetKcalResponseDto petKcal = petService.getPetKcal(username, petId, date);
            return new ResponseEntity(BaseResponse.createSuccess(petKcal), HttpStatus.OK);
        }
    }

    @Operation(summary = "반려견이 하루동안 섭취해야 할 적정 칼로리 조회.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "반려견 성공적 정보 조회"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "조회하려는 반려견이 본인의 반려견이 아닌 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "잘못된 petId"),
    })
    @GetMapping("/pets/{petId}/kcal/proper")
    public ResponseEntity<BaseResponse> readPetProperKcal(@CurrentUserUsername String username, @PathVariable Long petId) {
        ReadPetKcalResponseDto petProperKcal = petService.getPetProperKcal(username, petId);
        return new ResponseEntity(BaseResponse.createSuccess(petProperKcal), HttpStatus.OK);
    }

    @Operation(summary = "반려견이 하루동안 섭취해야 할 적정 칼로리 대비 실 섭취 칼로리 비율 조회.", description = "예)적정 섭취 칼로리가 100kcal인데 오늘 200kcal을 섭취한 경우, ratio=2를 반환함\n문제점) 오늘이 아닌 이전의 정보를 조회시 정확도에 문제가 존재함")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "반려견 성공적 정보 조회"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "조회하려는 반려견이 본인의 반려견이 아닌 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "잘못된 petId, 해당 일자에 식사 내역이 없는 경우"),
    })
    @GetMapping("/pets/{petId}/kcal/ratio")
    public ResponseEntity<BaseResponse> readPetKcalRatio(@CurrentUserUsername String username, @PathVariable Long petId,
                                                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        if (date == null) {
            ReadPetKcalRatioResponseDto petKcalRatioToday = petService.getPetKcalRatioToday(username, petId);
            return new ResponseEntity(BaseResponse.createSuccess(petKcalRatioToday), HttpStatus.OK);
        } else {
            ReadPetKcalRatioResponseDto petKcalRatio = petService.getPetKcalRatio(username, petId, date);
            return new ResponseEntity(BaseResponse.createSuccess(petKcalRatio), HttpStatus.OK);
        }
    }
}
