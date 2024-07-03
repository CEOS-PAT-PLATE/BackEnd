package com.petplate.petplate.petdailymeal.dto.request;

import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateDailyRawRequestDto {
    private Long rawId;
    @Min(10) // 섭취량 최소량은 10g
    private double serving;
}
