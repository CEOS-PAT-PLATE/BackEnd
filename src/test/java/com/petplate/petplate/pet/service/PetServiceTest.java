package com.petplate.petplate.pet.service;

import com.petplate.petplate.pet.domain.Activity;
import com.petplate.petplate.pet.domain.Neutering;
import com.petplate.petplate.pet.domain.entity.Pet;
import com.petplate.petplate.pet.dto.request.AddPetRequestDto;
import com.petplate.petplate.user.domain.Role;
import com.petplate.petplate.user.domain.entity.MemberShip;
import com.petplate.petplate.user.domain.entity.User;
import com.petplate.petplate.user.domain.entity.UserMemberShip;
import com.petplate.petplate.user.repository.MemberShipRepository;
import com.petplate.petplate.user.repository.UserMemberShipRepository;
import com.petplate.petplate.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
class PetServiceTest {
    @Autowired
    private PetService petService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MemberShipRepository memberShipRepository;
    @Autowired
    private UserMemberShipRepository userMemberShipRepository;

    private Long user1Id = null;
    private Long user2Id = null;
    private Long user3Id = null;
    private Long user4Id = null;

    @BeforeEach
    void setUp() {
        // MemberShip
        MemberShip memberShip = new MemberShip("1년짜리 멤버십", 10000, 365);
        memberShipRepository.save(memberShip);

        // User1(3 Pet, Membership)
        User user1 =
                User.builder().name("user1").role(Role.ADMIN)
                        .username("aaaa").password("aaaa")
                        .activated(true).isReceiveAd(true)
                        .phoneNumber("010-1111-1111")
                        .build();

        user1Id = userRepository.save(user1).getId();
        UserMemberShip user1MemberShip = new UserMemberShip(memberShip, user1);
        userMemberShipRepository.save(user1MemberShip);

        AddPetRequestDto pet1Dto =
                AddPetRequestDto.builder()
                        .name("pet1")
                        .age(3).weight(5).activity(Activity.ACTIVE)
                        .neutering(Neutering.INTACT).build();

        AddPetRequestDto pet2Dto =
                AddPetRequestDto.builder()
                        .name("pet2")
                        .age(3).weight(5).activity(Activity.SOMEWHAT_ACTIVE)
                        .neutering(Neutering.INTACT)
                        .build();

        AddPetRequestDto pet3Dto =
                AddPetRequestDto.builder()
                        .name("pet3")
                        .age(3).weight(5).activity(Activity.SOMEWHAT_ACTIVE)
                        .neutering(Neutering.INTACT)
                        .build();

        petService.addPet(user1Id, pet1Dto);
        petService.addPet(user1Id, pet2Dto);
        petService.addPet(user1Id, pet3Dto);

        // User2(3 Pet, Membership)
        User user2 =
                User.builder().name("user2").role(Role.ADMIN)
                        .username("bbbb").password("bbbb")
                        .activated(true).isReceiveAd(true)
                        .phoneNumber("010-2222-2222")
                        .build();

        user2Id = userRepository.save(user2).getId();
        UserMemberShip user2MemberShip = new UserMemberShip(memberShip, user2);
        userMemberShipRepository.save(user2MemberShip);

        AddPetRequestDto pet4Dto =
                AddPetRequestDto.builder()
                        .name("pet4")
                        .age(5).weight(15).activity(Activity.ACTIVE)
                        .neutering(Neutering.INTACT)
                        .build();

        AddPetRequestDto pet5Dto =
                AddPetRequestDto.builder()
                        .name("pet5")
                        .age(5).weight(15).activity(Activity.VERY_ACTIVE)
                        .neutering(Neutering.INTACT)
                        .build();

        AddPetRequestDto pet6Dto =
                AddPetRequestDto.builder()
                        .name("pet6")
                        .age(5).weight(15).activity(Activity.SOMEWHAT_ACTIVE)
                        .neutering(Neutering.INTACT)
                        .build();

        petService.addPet(user2Id, pet4Dto);
        petService.addPet(user2Id, pet5Dto);
        petService.addPet(user2Id, pet6Dto);

        // User3(1 Pet, No Membership)
        User user3 =
                User.builder().name("user3").role(Role.ADMIN)
                        .username("cccc").password("cccc")
                        .activated(true).isReceiveAd(true)
                        .phoneNumber("010-3333-3333")
                        .build();

        user3Id = userRepository.save(user3).getId();

        AddPetRequestDto pet7Dto =
                AddPetRequestDto.builder()
                        .name("pet7")
                        .age(7).weight(10).activity(Activity.ACTIVE)
                        .neutering(Neutering.INTACT)
                        .build();

        petService.addPet(user3Id, pet7Dto);

        // User4 (0 Pet, No Membership)
        User user4 =
                User.builder().name("user4").role(Role.ADMIN)
                        .username("dddd").password("dddd")
                        .activated(true).isReceiveAd(true)
                        .phoneNumber("010-4444-4444")
                        .build();

        user4Id = userRepository.save(user4).getId();
    }

    @Test
    @DisplayName("펫 등록 테스트")
    void addPet() {
        AddPetRequestDto newPetDto =
                AddPetRequestDto.builder()
                        .name("newPet")
                        .age(8).weight(13).activity(Activity.ACTIVE)
                        .neutering(Neutering.INTACT)
                        .build();

        User user1 = userRepository.findById(user1Id).get(); // pet 3, membership 보유
        petService.addPet(user1Id, newPetDto);  // 등록 가능

        User user4 = userRepository.findById(user4Id).get(); // pet 0, membership 없음
        petService.addPet(user4Id, newPetDto);  // 등록 불가능

        User user3 = userRepository.findById(user3Id).get(); // pet 1, membership 없음
        petService.addPet(user3Id, newPetDto);  // 등록 불가능


    }

    @Test
    void getAllPets() {
    }

    @Test
    void getPet() {
    }

    @Test
    void updatePetInfo() {
    }

    @Test
    void updateProfileImg() {
    }
}