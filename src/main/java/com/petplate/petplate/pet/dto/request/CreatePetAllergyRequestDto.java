package com.petplate.petplate.pet.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePetAllergyRequestDto {


    @NotBlank(message = "값이 입력되지 않았습니다.")
    private Long allergyId;
}