package com.petplate.petplate.pet.domain;

import lombok.Getter;

public enum ProfileImg {
    // 예시
    img1("usr/content/img1.jpg"),
    img2("usr/content/img2.jpg"),
    img3("usr/content/img3.jpg");

    @Getter
    private final String imgPath;

    ProfileImg(String imgPath) {
        this.imgPath = imgPath;
    }
}
