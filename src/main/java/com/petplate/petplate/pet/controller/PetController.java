package com.petplate.petplate.pet.controller;

import com.petplate.petplate.auth.interfaces.CurrentUserUsername;
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
    public ResponseEntity<BaseResponse> createPet(@CurrentUserUsername String username, @RequestBody @Valid AddPetRequestDto requestDto) {
        Pet pet = petService.addPet(username, requestDto);

        return new ResponseEntity(BaseResponse.createSuccess(pet.getName()), HttpStatus.CREATED);
    }

    @GetMapping("/pets")
    public ResponseEntity<BaseResponse> readAllPets(@CurrentUserUsername String username) {
        List<ReadPetResponseDto> allPets = petService.getAllPets(username);

        return new ResponseEntity(BaseResponse.createSuccess(allPets), HttpStatus.OK);
    }

    @GetMapping("/pets/{petId}")
    public ResponseEntity<BaseResponse> readAllPets(@CurrentUserUsername String username, @PathVariable Long petId) {
        ReadPetResponseDto pet = petService.getPet(username, petId);

        return new ResponseEntity(BaseResponse.createSuccess(pet), HttpStatus.OK);
    }

    @PutMapping("/pets/{petId}")
    public ResponseEntity<BaseResponse> modifyPet(@CurrentUserUsername String username, @PathVariable Long petId, @RequestBody @Valid ModifyPetInfoRequestDto requestDto) {
        petService.updatePetInfo(username, petId, requestDto);

        return new ResponseEntity(BaseResponse.createSuccess(null), HttpStatus.OK);
    }

    @GetMapping("/pets/images")
    public ResponseEntity<BaseResponse> readPetProfileImage(@CurrentUserUsername String username) {
        List<ReadPetProfileImageResponseDto> petProfileImages = petService.getPetProfileImages(username);

        return new ResponseEntity(BaseResponse.createSuccess(petProfileImages), HttpStatus.OK);
    }

    @PostMapping("/pets/{petId}/images")
    public ResponseEntity<BaseResponse> modifyPetProfileImage(@CurrentUserUsername String username, @PathVariable Long petId, @Valid ModifyPetProfileImgRequestDto requestDto) {
        petService.updateProfileImg(username, petId, requestDto);

        return new ResponseEntity(BaseResponse.createSuccess(null), HttpStatus.OK);
    }

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

    @GetMapping("/pets/{petId}/nutrient/sufficient")
    public ResponseEntity<BaseResponse> readPetSufficientNutrients(@CurrentUserUsername String username, @PathVariable Long petId,
                                                                   @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        List<ReadPetNutrientResponseDto> sufficientNutrient = petService.getSufficientNutrient(username, petId, date);
        return new ResponseEntity(BaseResponse.createSuccess(sufficientNutrient), HttpStatus.OK);
    }

    @GetMapping("/pets/{petId}/nutrient/deficient")
    public ResponseEntity<BaseResponse> readPetDeficientNutrients(@CurrentUserUsername String username, @PathVariable Long petId,
                                                                  @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        List<ReadPetNutrientResponseDto> deficientNutrient = petService.getDeficientNutrient(username, petId, date);
        return new ResponseEntity(BaseResponse.createSuccess(deficientNutrient), HttpStatus.OK);
    }

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

    @GetMapping("/pets/{petId}/kcal/proper")
    public ResponseEntity<BaseResponse> readPetProperKcal(@CurrentUserUsername String username, @PathVariable Long petId) {
        ReadPetKcalResponseDto petProperKcal = petService.getPetProperKcal(username, petId);
        return new ResponseEntity(BaseResponse.createSuccess(petProperKcal), HttpStatus.OK);
    }

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
