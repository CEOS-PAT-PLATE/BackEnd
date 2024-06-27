package com.petplate.petplate.pet.dto.request;

import com.petplate.petplate.pet.domain.Activity;
import com.petplate.petplate.pet.domain.Neutering;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ModifyPetInfoRequestDto {
    private String name;
    private Integer age;
    private Double weight;
    private Activity activity;
    private Neutering neutering;
}
