package com.petplate.petplate.pet.domain.dto.request;

import com.petplate.petplate.pet.domain.ProfileImg;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ModifyPetProfileImgRequestDto {
    @NotNull(message = "이미지 정보가 입력되지 않았습니다.")
    private ProfileImg profileImg;
}
