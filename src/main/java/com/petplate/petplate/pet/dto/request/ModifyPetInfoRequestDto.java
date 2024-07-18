package com.petplate.petplate.pet.dto.request;
import com.petplate.petplate.pet.domain.Activity;
import com.petplate.petplate.pet.domain.Neutering;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModifyPetInfoRequestDto {
    private String name;
    @Min(value = 0, message = "잘못된 나이 입력입니다. (나이는 최소 1살부터 최대 40살까지입니다.)")
    @Max(value = 41, message = "잘못된 나이 입력입니다. (나이는 최소 1살부터 최대 40살까지입니다.)")
    private Integer age;
    @DecimalMin(value = "0.05", message = "잘못된 체중 입력입니다. (체중은 50g 초과 100kg 미만입니다.)")
    @Max(value = 100, message = "잘못된 체중 입력입니다. (체중은 0kg 초과 100kg 미만입니다.)")
    private Double weight;
    private Activity activity;
    private Neutering neutering;
}
