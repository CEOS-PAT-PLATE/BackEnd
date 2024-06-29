package com.petplate.petplate.drug.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DrugFindRequestDto {

    @Size(min=1,message = "최소 1개 이상 입력")
    @Schema(description = "영양제를 여러 개 입력해주세요")
    List<String> nutrients = new ArrayList<>();

    @Builder
    public DrugFindRequestDto(List<String> nutrients) {
        this.nutrients = nutrients;
    }
}
