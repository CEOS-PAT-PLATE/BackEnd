package com.petplate.petplate.petdailymeal.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateDailyRawRequestDto {
    @NotBlank(message = "자연식이 입력되지 않았습니다.")
    private Long rawId;
    @Min(value = 10, message = "최소 섭취량은 10g입니다.") // 섭취량 최소량은 10g
    private double serving;
}
