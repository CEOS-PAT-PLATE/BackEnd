package com.petplate.petplate.pet.dto.request;

import com.petplate.petplate.pet.domain.ProfileImg;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModifyPetProfileImgRequestDto {
    @NotNull(message = "이미지 정보가 입력되지 않았습니다.")
    private ProfileImg profileImg;
}
