package com.petplate.petplate.pet.controller;

import com.petplate.petplate.auth.interfaces.CurrentUserPK;
import com.petplate.petplate.common.response.BaseResponse;
import com.petplate.petplate.pet.domain.entity.Pet;
import com.petplate.petplate.pet.dto.request.AddPetRequestDto;
import com.petplate.petplate.pet.dto.request.ModifyPetInfoRequestDto;
import com.petplate.petplate.pet.dto.request.ModifyPetProfileImgRequestDto;
import com.petplate.petplate.pet.dto.response.*;
import com.petplate.petplate.pet.service.PetService;
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
public class PetController {

    private final PetService petService;

    @PostMapping("/pets")
    public ResponseEntity<BaseResponse> createPet(@CurrentUserPK Long userId, @RequestBody @Valid AddPetRequestDto requestDto) {
        Pet pet = petService.addPet(userId, requestDto);

        return new ResponseEntity(BaseResponse.createSuccess(pet.getName()), HttpStatus.CREATED);
    }

    @GetMapping("/pets")
    public ResponseEntity<BaseResponse> readAllPets(@CurrentUserPK Long userId) {
        List<ReadPetResponseDto> allPets = petService.getAllPets(userId);

        return new ResponseEntity(BaseResponse.createSuccess(allPets), HttpStatus.OK);
    }

    @GetMapping("/pets/{petId}")
    public ResponseEntity<BaseResponse> readAllPets(@CurrentUserPK Long userId, @PathVariable Long petId) {
        ReadPetResponseDto pet = petService.getPet(userId, petId);

        return new ResponseEntity(BaseResponse.createSuccess(pet), HttpStatus.OK);
    }

    @PutMapping("/pets/{petId}")
    public ResponseEntity<BaseResponse> modifyPet(@CurrentUserPK Long userId, @PathVariable Long petId, @RequestBody @Valid ModifyPetInfoRequestDto requestDto) {
        petService.updatePetInfo(userId, petId, requestDto);

        return new ResponseEntity(BaseResponse.createSuccess(null), HttpStatus.OK);
    }

    @PostMapping("/pets/{petId}/images")
    public ResponseEntity<BaseResponse> modifyPetProfileImage(@CurrentUserPK Long userId, @PathVariable Long petId, @Valid ModifyPetProfileImgRequestDto requestDto) {
        petService.updateProfileImg(userId, petId, requestDto);

        return new ResponseEntity(BaseResponse.createSuccess(null), HttpStatus.OK);
    }

    @GetMapping("/pets/{petId}/nutrients")
    public ResponseEntity<BaseResponse> readPetNutrients(@CurrentUserPK Long userId, @PathVariable Long petId,
                                                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        if (date == null) {
            List<ReadPetNutrientResponseDto> petNutrientToday = petService.getPetNutrientToday(userId, petId);
            return new ResponseEntity(BaseResponse.createSuccess(petNutrientToday), HttpStatus.OK);
        } else {
            List<ReadPetNutrientResponseDto> petNutrient = petService.getPetNutrient(userId, petId, date);
            return new ResponseEntity(BaseResponse.createSuccess(petNutrient), HttpStatus.OK);
        }
    }

    @GetMapping("/pets/{petId}/nutrients/ratio")
    public ResponseEntity<BaseResponse> readPetNutrientRatio(@CurrentUserPK Long userId, @PathVariable Long petId,
                                                             @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        if (date == null) {
            List<ReadPetNutrientRatioResponseDto> petNutrientRatioToday = petService.getPetNutrientRatioToday(userId, petId);
            return new ResponseEntity(BaseResponse.createSuccess(petNutrientRatioToday), HttpStatus.OK);
        } else {
            List<ReadPetNutrientRatioResponseDto> petNutrientRatio = petService.getPetNutrientRatio(userId, petId, date);
            return new ResponseEntity(BaseResponse.createSuccess(petNutrientRatio), HttpStatus.OK);
        }
    }

    @GetMapping("/pets/{petId}/nutrient/sufficient")
    public ResponseEntity<BaseResponse> readPetSufficientNutrients(@CurrentUserPK Long userId, @PathVariable Long petId,
                                                                   @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        List<ReadPetNutrientResponseDto> sufficientNutrient = petService.getSufficientNutrient(userId, petId, date);
        return new ResponseEntity(BaseResponse.createSuccess(sufficientNutrient), HttpStatus.OK);
    }

    @GetMapping("/pets/{petId}/nutrient/deficient")
    public ResponseEntity<BaseResponse> readPetDeficientNutrients(@CurrentUserPK Long userId, @PathVariable Long petId,
                                                                  @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        List<ReadPetNutrientResponseDto> deficientNutrient = petService.getDeficientNutrient(userId, petId, date);
        return new ResponseEntity(BaseResponse.createSuccess(deficientNutrient), HttpStatus.OK);
    }

    @GetMapping("/pets/{petId}/kcal")
    public ResponseEntity<BaseResponse> readPetKcal(@CurrentUserPK Long userId, @PathVariable Long petId,
                                                    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        if (date == null) {
            ReadPetKcalResponseDto petKcalToday = petService.getPetKcalToday(userId, petId);
            return new ResponseEntity(BaseResponse.createSuccess(petKcalToday), HttpStatus.OK);
        } else {
            ReadPetKcalResponseDto petKcal = petService.getPetKcal(userId, petId, date);
            return new ResponseEntity(BaseResponse.createSuccess(petKcal), HttpStatus.OK);
        }
    }

    @GetMapping("/pets/{petId}/kcal/proper")
    public ResponseEntity<BaseResponse> readPetProperKcal(@CurrentUserPK Long userId, @PathVariable Long petId) {
        ReadPetKcalResponseDto petProperKcal = petService.getPetProperKcal(userId, petId);
        return new ResponseEntity(BaseResponse.createSuccess(petProperKcal), HttpStatus.OK);
    }

    @GetMapping("/pets/{petId}/kcal/ratio")
    public ResponseEntity<BaseResponse> readPetKcalRatio(@CurrentUserPK Long userId, @PathVariable Long petId,
                                                    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        if (date == null) {
            ReadPetKcalRatioResponseDto petKcalRatioToday = petService.getPetKcalRatioToday(userId, petId);
            return new ResponseEntity(BaseResponse.createSuccess(petKcalRatioToday), HttpStatus.OK);
        } else {
            ReadPetKcalRatioResponseDto petKcalRatio = petService.getPetKcalRatio(userId, petId, date);
            return new ResponseEntity(BaseResponse.createSuccess(petKcalRatio), HttpStatus.OK);
        }
    }
}
