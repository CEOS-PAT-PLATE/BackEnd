package com.petplate.petplate.pet.dto.response;

import com.petplate.petplate.pet.domain.ProfileImg;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ReadPetProfileImageResponseDto {
    private String name;
    private String imgPath;

    public static ReadPetProfileImageResponseDto from(ProfileImg img) {
        ReadPetProfileImageResponseDto response = new ReadPetProfileImageResponseDto();
        response.name = img.getName();
        response.imgPath = img.getImgPath();

        return response;
    }
}
