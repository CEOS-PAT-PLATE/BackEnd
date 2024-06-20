package com.petplate.petplate.pet.domain.service;

import com.petplate.petplate.common.response.error.ErrorCode;
import com.petplate.petplate.common.response.error.exception.BadRequestException;
import com.petplate.petplate.common.response.error.exception.NotFoundException;
import com.petplate.petplate.pet.domain.dto.request.AddPetRequestDto;
import com.petplate.petplate.pet.domain.dto.request.ModifyPetInfoRequestDto;
import com.petplate.petplate.pet.domain.dto.request.ModifyPetProfileImgRequestDto;
import com.petplate.petplate.pet.domain.dto.response.ReadPetAllergyResponseDto;
import com.petplate.petplate.pet.domain.dto.response.ReadPetDiseaseResponseDto;
import com.petplate.petplate.pet.domain.dto.response.ReadPetResponseDto;
import com.petplate.petplate.pet.domain.entity.Pet;
import com.petplate.petplate.pet.domain.entity.PetAllergy;
import com.petplate.petplate.pet.domain.repository.PetAllergyRepository;
import com.petplate.petplate.pet.domain.repository.PetDiseaseRepository;
import com.petplate.petplate.pet.domain.repository.PetRepository;
import com.petplate.petplate.user.domain.entity.User;
import com.petplate.petplate.user.repository.UserMemberShipRepository;
import com.petplate.petplate.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PetService {
    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final UserMemberShipRepository userMemberShipRepository;
    private final PetAllergyRepository petAllergyRepository;
    private final PetDiseaseRepository petDiseaseRepository;

    @Transactional
    public Pet addPet(Long userId, @Valid AddPetRequestDto requestDto) {
        // 해당 pk를 가지는 유저가 존재하지 않는 경우
        User owner = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));

        // 멤버십이 존재하지 않으면서 두마리 이상의 반려견을 추가하려는 경우 => 예외 발생
        if (!userMemberShipRepository.existsByUserId(userId) && petRepository.existsByOwnerId(userId)) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        Pet pet = Pet.builder()
                .name(requestDto.getName())
                .age(requestDto.getAge())
                .weight(requestDto.getAge())
                .activity(requestDto.getActivity())
                .isNeutering(requestDto.isNeutering())
                .owner(owner)
                .build();

        petRepository.save(pet);

        return pet;
    }

    public List<ReadPetResponseDto> readAllPets(Long userId) {
        List<ReadPetResponseDto> responses = new ArrayList<>();

        petRepository.findByOwnerId(userId)
                .forEach(pet -> responses.add(ReadPetResponseDto.from(pet)));
        return responses;
    }

    public ReadPetResponseDto readPet(Long userId, Long petId) {
        Pet pet = getPet(userId, petId);

        ReadPetResponseDto response = ReadPetResponseDto.from(pet);

        return response;
    }

    @Transactional
    public void modifyPetInfo (Long userId, Long petId, ModifyPetInfoRequestDto requestDto) {
        Pet pet = getPet(userId, petId);

        pet.updateInfo(requestDto.getName(), requestDto.getAge(), requestDto.getWeight(), requestDto.getActivity(), requestDto.getIsNeutering());
    }

    @Transactional
    public void modifyProfileImg(Long userId, Long petId, @Valid ModifyPetProfileImgRequestDto requestDto) {
        Pet pet = getPet(userId, petId);
        pet.updateProfileImg(requestDto.getProfileImg());
    }

    public List<ReadPetAllergyResponseDto> readAllergies(Long userId, Long petId) {
        List<ReadPetAllergyResponseDto> responses = new ArrayList<>();

        Pet pet = getPet(userId, petId);
        petAllergyRepository.findByPetId(petId).forEach(petAllergy->{
            responses.add(ReadPetAllergyResponseDto.from(petAllergy.getAllergy()));
        });

        return responses;
    }

    public List<ReadPetDiseaseResponseDto> readDiseases(Long userId, Long petId) {
        List<ReadPetDiseaseResponseDto> responses = new ArrayList<>();

        Pet pet = getPet(userId, petId);
        petDiseaseRepository.findByPetId(petId).forEach(petDisease -> {
            responses.add(ReadPetDiseaseResponseDto.from(petDisease.getDisease()));
        });

        return responses;
    }

    private Pet getPet(Long userId, Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));

        // 조회하려는 반려견이 본인의 반려견이 아닌 경우 예외 발생
        if (!pet.getOwner().getId().equals(userId)) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        return pet;
    }
}
