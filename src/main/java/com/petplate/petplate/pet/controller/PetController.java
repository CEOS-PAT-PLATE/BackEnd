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
    private static final String OK = "200";
    private static final String CREATED = "201";
    private static final String BAD_REQUEST = "400";
    private static final String NOT_FOUND = "404";

    @Operation(summary = "반려견 추가", description = "1) Neutering(중성화)에 들어갈 값: INTACT(중성화 안함), NEUTERED(중성화 함) " +
            "2) 활동량(Activity)에 들어갈 값: INACTIVE(차분), SOMEWHAT_ACTIVE(보통), ACTIVE(활발), VERY_ACTIVE(초활발)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = CREATED, description = "반려견 성공적 추가"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "멤버십이 존재하지 않으면서 두마리 이상의 반려견을 추가하려는 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "해당 username을 가지는 유저가 존재하지 않는 경우")
    })
    @PostMapping("/pets")
    public ResponseEntity<BaseResponse<Long>> createPet(@CurrentUserUsername String username, @RequestBody @Valid CreatePetRequestDto requestDto) {
        Pet pet = petService.createPet(username, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.createSuccess(pet.getId()));
    }

    @Operation(summary = "유저의 모든 반려견 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "반려견 성공적 조회"),
    })
    @GetMapping("/pets")
    public ResponseEntity<BaseResponse<List<ReadPetResponseDto>>> readAllPets(@CurrentUserUsername String username) {
        List<ReadPetResponseDto> allPets = petService.getAllPets(username);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.createSuccess(allPets));
    }

    @Operation(summary = "petId로 유저의 특정 반려견 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "반려견 성공적 조회"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "조회하려는 반려견이 본인의 반려견이 아닌 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "존재하지 않는 petId"),
    })
    @GetMapping("/pets/{petId}")
    public ResponseEntity<BaseResponse<ReadPetResponseDto>> readAllPets(@CurrentUserUsername String username, @PathVariable("petId") Long petId) {
        ReadPetResponseDto pet = petService.getPet(username, petId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.createSuccess(pet));
    }

    @Operation(summary = "반려견 정보 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "반려견 성공적 정보 수정"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "조회하려는 반려견이 본인의 반려견이 아닌 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "존재하지 않는 petId"),
    })
    @PutMapping("/pets/{petId}")
    public ResponseEntity<BaseResponse> modifyPet(@CurrentUserUsername String username, @PathVariable("petId") Long petId, @RequestBody @Valid ModifyPetInfoRequestDto requestDto) {
        petService.updatePetInfo(username, petId, requestDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.createSuccess(null));
    }

    @Operation(summary = "선택할 수 있는 프로필 이미지들 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "프로필 이미지 성공적 조회"),
    })
    @GetMapping("/pets/images")
    public ResponseEntity<BaseResponse<List<ReadPetProfileImageResponseDto>>> readPetProfileImages() {
        List<ReadPetProfileImageResponseDto> petProfileImages = petService.getPetProfileImages();

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.createSuccess(petProfileImages));
    }

    @Operation(summary = "프로필 이미지 이름으로 선택한 프로필 이미지로 반려견 프로필 이미지 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "반려견 성공적 정보 수정"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "조회하려는 반려견이 본인의 반려견이 아닌 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "존재하지 않는 petId, 존재하지 않는 이미지 이름 (name)"),
    })
    @PostMapping("/pets/{petId}/images")
    public ResponseEntity<BaseResponse> modifyPetProfileImage(@CurrentUserUsername String username, @PathVariable("petId") Long petId, @Valid @RequestBody ModifyPetProfileImgRequestDto requestDto) {
        petService.updateProfileImg(username, petId, requestDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.createSuccess(null));
    }

    @Operation(summary = "반려견이 하루 섭취한 영양소 정보 조회. (날짜 미입력시 오늘 정보 조회)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "반려견 성공적 정보 조회"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "조회하려는 반려견이 본인의 반려견이 아닌 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "존재하지 않는 petId, 해당 일자에 식사 내역이 없는 경우"),
    })
    @GetMapping("/pets/{petId}/nutrients")
    public ResponseEntity<BaseResponse<List<ReadPetNutrientResponseDto>>> readPetNutrients(@CurrentUserUsername String username, @PathVariable("petId") Long petId,
                                                         @RequestParam(value = "date",required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        if (date == null) {
            List<ReadPetNutrientResponseDto> petNutrientToday = petService.getPetNutrientToday(username, petId);
            return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.createSuccess(petNutrientToday));
        } else {
            List<ReadPetNutrientResponseDto> petNutrient = petService.getPetNutrient(username, petId, date);
            return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.createSuccess(petNutrient));
        }
    }

    @Operation(summary = "반려견이 하루동안 섭취한 칼로리 조회. (날짜 미입력시 오늘 정보 조회)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "반려견 성공적 정보 조회"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "조회하려는 반려견이 본인의 반려견이 아닌 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "존재하지 않는 petId, 해당 일자에 식사 내역이 없는 경우"),
    })
    @GetMapping("/pets/{petId}/kcal")
    public ResponseEntity<BaseResponse<ReadPetKcalResponseDto>> readPetKcal(@CurrentUserUsername String username, @PathVariable("petId") Long petId,
                                                    @RequestParam(value = "date",required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        if (date == null) {
            ReadPetKcalResponseDto petKcalToday = petService.getPetKcalToday(username, petId);
            return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.createSuccess(petKcalToday));
        } else {
            ReadPetKcalResponseDto petKcal = petService.getPetKcal(username, petId, date);
            return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.createSuccess(petKcal));
        }
    }

    @Operation(summary = "반려견이 하루동안 섭취해야 할 적정 칼로리 조회.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "반려견 성공적 정보 조회"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "조회하려는 반려견이 본인의 반려견이 아닌 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "존재하지 않는 petId"),
    })
    @GetMapping("/pets/{petId}/kcal/proper")
    public ResponseEntity<BaseResponse<ReadPetKcalResponseDto>> readPetProperKcal(@CurrentUserUsername String username, @PathVariable("petId") Long petId) {
        ReadPetKcalResponseDto petProperKcal = petService.getPetProperKcal(username, petId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.createSuccess(petProperKcal));
    }

    @Operation(summary = "반려견이 하루동안 섭취해야 할 적정 칼로리 대비 실 섭취 칼로리 비율 조회.", description = "예)적정 섭취 칼로리가 100kcal인데 오늘 200kcal을 섭취한 경우, ratio=2를 반환함\n문제점) 오늘이 아닌 이전의 정보를 조회시 정확도에 문제가 존재함")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "반려견 성공적 정보 조회"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "조회하려는 반려견이 본인의 반려견이 아닌 경우"),
            @ApiResponse(responseCode = NOT_FOUND, description = "존재하지 않는 petId, 해당 일자에 식사 내역이 없는 경우"),
    })
    @GetMapping("/pets/{petId}/kcal/ratio")
    public ResponseEntity<BaseResponse<ReadPetKcalRatioResponseDto>> readPetKcalRatio(@CurrentUserUsername String username, @PathVariable("petId") Long petId,
                                                         @RequestParam(value = "date",required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        if (date == null) {
            ReadPetKcalRatioResponseDto petKcalRatioToday = petService.getPetKcalRatioToday(username, petId);
            return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.createSuccess(petKcalRatioToday));
        } else {
            ReadPetKcalRatioResponseDto petKcalRatio = petService.getPetKcalRatio(username, petId, date);
            return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.createSuccess(petKcalRatio));
        }
    }
}
