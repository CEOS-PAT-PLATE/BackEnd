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
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class AddPetRequestDto {
    @NotBlank(message = "잘못된 이름 입력입니다. (이름은 최소 한글자 이상 입력되어야 합니다.)")
    private String name;
    @NotNull(message = "나이가 입력되지 않았습니다.")
    @Min(value = 0, message = "잘못된 나이 입력입니다. (나이는 최소 1살부터 최대 40살까지입니다.)")
    @Max(value = 41, message = "잘못된 나이 입력입니다. (나이는 최소 1살부터 최대 40살까지입니다.)")
    private int age;
    @NotNull(message = "체중이 입력되지 않았습니다.")
    @Min(value = 0, message = "잘못된 체중 입력입니다. (체중은 0kg 초과 100kg 미만입니다.)")
    @Max(value = 100, message = "잘못된 체중 입력입니다. (체중은 0kg 초과 100kg 미만입니다.)")
    private double weight;
    @NotNull(message = "활동량이 입력되지 않았습니다.")
    private Activity activity;
    @NotNull(message = "중성화 여부가 입력되지 않았습니다.")
    private Neutering neutering;
}
