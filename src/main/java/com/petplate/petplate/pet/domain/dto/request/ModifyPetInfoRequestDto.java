package com.petplate.petplate.pet.domain.dto.request;

import com.petplate.petplate.pet.domain.Activity;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ModifyPetInfoRequestDto {
    private String name;
    private Integer age;
    private Double weight;
    private Activity activity;
    private Boolean isNeutering;
}
