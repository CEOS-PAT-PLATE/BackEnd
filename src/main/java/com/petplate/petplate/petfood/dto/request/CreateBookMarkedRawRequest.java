package com.petplate.petplate.petfood.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateBookMarkedRawRequest {
    @NotBlank(message = "자연식이 입력되지 않았습니다")
    private Long rawId;

    @NotBlank(message = "섭취량이 입력되지 않았습니다")
    @Min(value = 10, message = "최소 섭취량은 10g입니다")
    private double serving;
}
