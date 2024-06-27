package com.petplate.petplate.pet.dto.response;

import com.petplate.petplate.pet.domain.Activity;
import com.petplate.petplate.pet.domain.Neutering;
import com.petplate.petplate.pet.domain.entity.Pet;
import lombok.Getter;

@Getter
public class ReadPetResponseDto {
    private Long id;
    private String name;
    private int age;
    private double weight;
    private Activity activity;
    private Neutering neutering;
    private String profileImgPath;

    public static ReadPetResponseDto from(Pet pet) {
        ReadPetResponseDto response = new ReadPetResponseDto();

        response.id = pet.getId();
        response.name = pet.getName();
        response.age = pet.getAge();
        response.weight = pet.getWeight();
        response.activity = pet.getActivity();
        response.neutering = pet.getNeutering();
        response.profileImgPath = pet.getProfileImg().name();

        return response;
    }
}
