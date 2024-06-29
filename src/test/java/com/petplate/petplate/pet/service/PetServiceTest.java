package com.petplate.petplate.pet.service;

import com.petplate.petplate.common.response.error.exception.BadRequestException;
import com.petplate.petplate.common.response.error.exception.NotFoundException;
import com.petplate.petplate.medicalcondition.domain.entity.Allergy;
import com.petplate.petplate.medicalcondition.domain.entity.Disease;
import com.petplate.petplate.medicalcondition.repository.AllergyRepository;
import com.petplate.petplate.medicalcondition.repository.DiseaseRepository;
import com.petplate.petplate.pet.domain.Activity;
import com.petplate.petplate.pet.domain.Neutering;

import com.petplate.petplate.pet.domain.ProfileImg;
import com.petplate.petplate.pet.domain.entity.Pet;
import com.petplate.petplate.pet.dto.request.*;
import com.petplate.petplate.pet.dto.response.ReadPetDiseaseResponseDto;
import com.petplate.petplate.pet.dto.response.ReadPetResponseDto;
import com.petplate.petplate.pet.repository.PetAllergyRepository;
import com.petplate.petplate.pet.repository.PetDiseaseRepository;
import com.petplate.petplate.pet.repository.PetRepository;
import com.petplate.petplate.user.domain.Role;
import com.petplate.petplate.user.domain.SocialType;
import com.petplate.petplate.user.domain.entity.MemberShip;
import com.petplate.petplate.user.domain.entity.User;
import com.petplate.petplate.user.domain.entity.UserMemberShip;
import com.petplate.petplate.user.repository.MemberShipRepository;
import com.petplate.petplate.user.repository.UserMemberShipRepository;
import com.petplate.petplate.user.repository.UserRepository;
import io.jsonwebtoken.lang.Assert;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class PetServiceTest {
    @Autowired
    private PetRepository petRepository;
    @Autowired
    private PetService petService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MemberShipRepository memberShipRepository;
    @Autowired
    private UserMemberShipRepository userMemberShipRepository;
    @Autowired
    private AllergyRepository allergyRepository;
    @Autowired
    private DiseaseRepository diseaseRepository;

    private Long user1Id = null;
    private Long user2Id = null;
    private Long user3Id = null;
    private Long user4Id = null;

    private Long pet1Id = null;
    @Autowired
    private PetAllergyRepository petAllergyRepository;
    @Autowired
    private PetDiseaseRepository petDiseaseRepository;

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
                        .socialType(SocialType.NAVER)
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
                        .age(3).weight(5).activity(Activity.INACTIVE)
                        .neutering(Neutering.INTACT)

                        .build();

        pet1Id = petService.addPet(user1Id, pet1Dto).getId();
        petService.addPet(user1Id, pet2Dto);
        petService.addPet(user1Id, pet3Dto);

        // User2(3 Pet, Membership)
        User user2 =
                User.builder().name("user2").role(Role.ADMIN)
                        .username("bbbb").password("bbbb")
                        .activated(true).isReceiveAd(true)
                        .phoneNumber("010-2222-2222")
                        .socialType(SocialType.NAVER)
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
                        .age(5).weight(15).activity(Activity.SOMEWHAT_ACTIVE)
                        .neutering(Neutering.INTACT)
                        .build();

        AddPetRequestDto pet6Dto =
                AddPetRequestDto.builder()
                        .name("pet6")
                        .age(5).weight(15).activity(Activity.INACTIVE)
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
                        .socialType(SocialType.NAVER)
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
                        .socialType(SocialType.NAVER)
                        .build();

        user4Id = userRepository.save(user4).getId();

        // 알러지 저장
        Allergy allergy1 = Allergy.builder().name("당근").description("당근 못먹음").build();
        Allergy allergy2 = Allergy.builder().name("소고기").description("소고기 못먹음").build();
        allergyRepository.save(allergy1);
        allergyRepository.save(allergy2);

        // 질병 저장
        Disease disease1 = Disease.builder().name("눈").description("눈아픔").build();
        Disease disease2 = Disease.builder().name("눈물자국").description("눈물자국있음").build();
        Disease disease3 = Disease.builder().name("피부질환").description("피부나쁨").build();
        Disease disease4 = Disease.builder().name("장활동").description("장나쁨").build();
        Disease disease5 = Disease.builder().name("고관절").description("관절아픔").build();
        diseaseRepository.save(disease1);
        diseaseRepository.save(disease2);
        diseaseRepository.save(disease3);
        diseaseRepository.save(disease4);
        diseaseRepository.save(disease5);
    }

    @Test
    @DisplayName("펫 등록 테스트")
    void addPet() {
        // given
        AddPetRequestDto newPetDto =
                AddPetRequestDto.builder()
                        .name("newPet")
                        .age(8).weight(13).activity(Activity.ACTIVE)
                        .neutering(Neutering.INTACT).build();


        // when
        User user1 = userRepository.findById(user1Id).get(); // pet 3, membership 보유
        User user3 = userRepository.findById(user3Id).get(); // pet 1, membership 없음
        User user4 = userRepository.findById(user4Id).get(); // pet 0, membership 없음

        // then
        petService.addPet(user1Id, newPetDto);  // // pet 4, membership 보유 => 등록 가능
        assertThrows(BadRequestException.class, () -> petService.addPet(user3Id, newPetDto)); // pet 2, membership 없음 => 등록 불가능
        petService.addPet(user4Id, newPetDto);  // pet 1, membership 없음 => 등록 가능
        assertThrows(BadRequestException.class, () -> petService.addPet(user4Id, newPetDto)); // pet 2, membership 없음 => 등록 불가능
    }

    @Test
    @DisplayName("유저의 모든 펫 조회하기")
    void getAllPets() {
        //when
        petService.getAllPets(user1Id).forEach(System.out::println);

        //then
        Assertions.assertEquals(3, petService.getAllPets(user1Id).size());
        Assertions.assertEquals(3, petService.getAllPets(user2Id).size());
        Assertions.assertEquals(1, petService.getAllPets(user3Id).size());
        Assertions.assertEquals(0, petService.getAllPets(user4Id).size());
    }

    @Test
    @DisplayName("유저의 특정 펫 조회하기")
    void getPet() {
        // given
        List<ReadPetResponseDto> user1Pets = petService.getAllPets(user1Id);

        // when
        user1Pets.forEach(user1Pet -> {
            Long petId = user1Pet.getId();
            ReadPetResponseDto pet = petService.getPet(user1Id, petId);

            System.out.println("pet = " + pet);

            // then
            // 나이 3, 체중: 5, 중성화: intact
            Assertions.assertEquals(pet.getAge(), 3);
            Assertions.assertEquals(pet.getWeight(), 5);
            Assertions.assertEquals(pet.getNeutering(), Neutering.INTACT);

        });

        //then
        // 존재하지 않는 펫 조회시 예외 발생
        Assertions.assertThrows(NotFoundException.class,
                () -> petService.getPet(user1Id, 123456L));
    }

    @Test
    @DisplayName("펫 정보 수정하기")
    void updatePetInfo() {
        List<Pet> pets = petRepository.findByOwnerId(user1Id);
        Long petId = pets.get(0).getId();


        // 1) 펫의 모든 정보 업데이트 한 경우
        ModifyPetInfoRequestDto updateData = ModifyPetInfoRequestDto.builder()
                .petId(petId)
                .name("new name").weight(100D).activity(Activity.VERY_ACTIVE).age(100).neutering(Neutering.NEUTERED)

                .build();
        petService.updatePetInfo(user1Id, updateData);

        Pet updatedPet = petRepository.findById(petId).get();

        Assertions.assertEquals(updatedPet.getName(), "new name");
        Assertions.assertEquals(updatedPet.getWeight(), 100D);
        Assertions.assertEquals(updatedPet.getActivity(), Activity.VERY_ACTIVE);
        Assertions.assertEquals(updatedPet.getAge(), 100);
        Assertions.assertEquals(updatedPet.getNeutering(), Neutering.NEUTERED);


        // 2) 일부분(이름, 나이)만 수정한 경우 (수정한 부분만 업데이트되고 건들지 않은 부분은 기존 정보가 유진된다)
        // 이름: new name -> new name2
        // 나이: 100 -> 5
        ModifyPetInfoRequestDto updateData2 = ModifyPetInfoRequestDto.builder()
                .petId(petId)
                .name("new name2").age(5)
                .build();
        petService.updatePetInfo(user1Id, updateData2);

        Pet updatedPet2 = petRepository.findById(petId).get();

        Assertions.assertEquals(updatedPet2.getName(), "new name2");  // 변경됨
        Assertions.assertEquals(updatedPet2.getWeight(), updatedPet.getWeight());
        Assertions.assertEquals(updatedPet2.getActivity(), updatedPet.getActivity());
        Assertions.assertEquals(updatedPet2.getAge(), 5); // 변경됨
        Assertions.assertEquals(updatedPet2.getNeutering(), updatedPet.getNeutering());

        // 3) 자기 강아지가 아닌 경우 -> 예외 발생
        Assertions.assertThrows(BadRequestException.class, () -> petService.updatePetInfo(user2Id, updateData2));
    }

    @Test
    @DisplayName("프로필 이미지 업데이트")
    void updateProfileImg() {
        // given
        ModifyPetProfileImgRequestDto request = ModifyPetProfileImgRequestDto.builder()
                .petId(pet1Id)
                .profileImg(ProfileImg.img1)
                .build();

        // when
        petService.updateProfileImg(user1Id, request);
        Pet pet = petRepository.findById(request.getPetId()).get();

        // then
        Assertions.assertEquals(ProfileImg.img1, pet.getProfileImg());
    }

    @Test
    @DisplayName("펫에 알러지 등록")
    public void addPetAllergy() throws Exception{
        //given
        Allergy allergy1 = allergyRepository.findByName("당근").get();
        Allergy allergy2 = allergyRepository.findByName("소고기").get();

        AddPetAllergyRequestDto request1
                = AddPetAllergyRequestDto.builder().allergyId(allergy1.getId()).petId(pet1Id).build();
        AddPetAllergyRequestDto request2
                = AddPetAllergyRequestDto.builder().allergyId(allergy2.getId()).petId(pet1Id).build();

        //when
        petService.addPetAllergy(user1Id, request1);
        petService.addPetAllergy(user1Id, request2);

        //then
        Assertions.assertEquals(2, petAllergyRepository.findByPetId(pet1Id).size());
        Assertions.assertThrows(BadRequestException.class, () -> petService.addPetAllergy(user1Id, request2));  // 이미 보유한 질병 재등록시 예외 발생
    }

    @Test
    @DisplayName("펫에 질병 등록 및 조회")
    public void getPetAllergies() throws Exception{
        //given
        Disease disease1 = diseaseRepository.findByName("눈물자국").get();
        Disease disease2 = diseaseRepository.findByName("피부질환").get();
        Disease disease3 = diseaseRepository.findByName("고관절").get();

        AddPetDiseaseRequestDto request1 = AddPetDiseaseRequestDto.builder().diseaseId(disease1.getId()).petId(pet1Id).build();
        AddPetDiseaseRequestDto request2 = AddPetDiseaseRequestDto.builder().diseaseId(disease2.getId()).petId(pet1Id).build();
        AddPetDiseaseRequestDto request3 = AddPetDiseaseRequestDto.builder().diseaseId(disease3.getId()).petId(pet1Id).build();

        //when
        petService.addPetDisease(user1Id,request1);
        petService.addPetDisease(user1Id,request2);
        petService.addPetDisease(user1Id,request3);

        //then
        List<ReadPetDiseaseResponseDto> allDiseases = petService.getAllDiseases(user1Id, pet1Id);
        for (ReadPetDiseaseResponseDto allDisease : allDiseases) {
            System.out.println("allDisease = " + allDisease);
        }
        Assertions.assertEquals(3, allDiseases.size());
    }

}