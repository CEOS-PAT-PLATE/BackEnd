package com.petplate.petplate.pet.dto.request;
import com.petplate.petplate.pet.domain.Activity;
import com.petplate.petplate.pet.domain.Neutering;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
public class ModifyPetInfoRequestDto {
    @NotNull(message = "반려견이 선택되지 않았습니다")
    private Long petId;

    private String name;
    private Integer age;
    private Double weight;
    private Activity activity;
    private Neutering neutering;
}
