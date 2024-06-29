package com.petplate.petplate.pet.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddPetAllergyRequestDto {
    @NotNull(message = "반려견이 선택되지 않았습니다")
    private Long petId;

    @NotBlank(message = "값이 입력되지 않았습니다.")
    private Long allergyId;
}