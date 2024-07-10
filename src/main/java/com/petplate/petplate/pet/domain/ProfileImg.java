package com.petplate.petplate.pet.domain;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum ProfileImg {
    // 예시
    img1("img1","usr/content/img1.jpg"),
    img2("img2","usr/content/img2.jpg"),
    img3("img3","usr/content/img3.jpg");

    private final String name;
    private final String imgPath;

    ProfileImg(String name, String imgPath) {
        this.name = name;
        this.imgPath = imgPath;
    }

    public static ProfileImg getProfileImg(String name) {
        try {
            ProfileImg profileImg = ProfileImg.valueOf(name);
            return profileImg;
        } catch (Exception e) {
            return null;
        }
    }
}
