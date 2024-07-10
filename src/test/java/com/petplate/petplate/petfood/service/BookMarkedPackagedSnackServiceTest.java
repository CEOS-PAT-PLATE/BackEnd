package com.petplate.petplate.petfood.service;

import com.petplate.petplate.common.EmbeddedType.Nutrient;
import com.petplate.petplate.common.EmbeddedType.Vitamin;
import com.petplate.petplate.common.response.error.exception.BadRequestException;
import com.petplate.petplate.common.response.error.exception.NotFoundException;
import com.petplate.petplate.medicalcondition.domain.entity.Allergy;
import com.petplate.petplate.medicalcondition.domain.entity.Disease;
import com.petplate.petplate.medicalcondition.repository.AllergyRepository;
import com.petplate.petplate.medicalcondition.repository.DiseaseRepository;
import com.petplate.petplate.pet.domain.Activity;
import com.petplate.petplate.pet.domain.Neutering;
import com.petplate.petplate.pet.domain.entity.Pet;
import com.petplate.petplate.pet.dto.request.CreatePetRequestDto;
import com.petplate.petplate.pet.repository.PetRepository;
import com.petplate.petplate.pet.service.PetService;
import com.petplate.petplate.petdailymeal.domain.entity.DailyMeal;
import com.petplate.petplate.petdailymeal.repository.DailyMealRepository;
import com.petplate.petplate.petfood.domain.entity.BookMarkedPackagedSnack;
import com.petplate.petplate.petfood.dto.request.CreateBookMarkedPackagedSnackRequestDto;
import com.petplate.petplate.petfood.dto.response.ReadBookMarkedPackagedSnackResponseDto;
import com.petplate.petplate.petfood.repository.BookMarkedPackagedSnackRepository;
import com.petplate.petplate.user.domain.Role;
import com.petplate.petplate.user.domain.SocialType;
import com.petplate.petplate.user.domain.entity.MemberShip;
import com.petplate.petplate.user.domain.entity.User;
import com.petplate.petplate.user.domain.entity.UserMemberShip;
import com.petplate.petplate.user.repository.MemberShipRepository;
import com.petplate.petplate.user.repository.UserMemberShipRepository;
import com.petplate.petplate.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BookMarkedPackagedSnackServiceTest {

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
    private BookMarkedPackagedSnackRepository bookMarkedPackagedSnackRepository;
    @Autowired
    private BookMarkedPackagedSnackService bookMarkedPackagedSnackService;

    @Autowired
    private DailyMealRepository dailyMealRepository;

    private String user1Username = null;
    private String user2Username = null;
    private String user3Username = null;
    private String user4Username = null;

    private Long pet1Id = null;

    private Long bookMarkedPackagedSnack1Id = null;

    @BeforeEach
    public void each(){
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

        user1Username = userRepository.save(user1).getUsername();
        UserMemberShip user1MemberShip = new UserMemberShip(memberShip, user1);
        userMemberShipRepository.save(user1MemberShip);

        CreatePetRequestDto pet1Dto =
                CreatePetRequestDto.builder()
                        .name("pet1")
                        .age(3).weight(5).activity(Activity.ACTIVE)
                        .neutering(Neutering.INTACT).build();

        CreatePetRequestDto pet2Dto =
                CreatePetRequestDto.builder()
                        .name("pet2")
                        .age(3).weight(5).activity(Activity.SOMEWHAT_ACTIVE)
                        .neutering(Neutering.INTACT)

                        .build();

        CreatePetRequestDto pet3Dto =
                CreatePetRequestDto.builder()
                        .name("pet3")
                        .age(3).weight(5).activity(Activity.INACTIVE)
                        .neutering(Neutering.INTACT)

                        .build();

        pet1Id = petService.createPet(user1Username, pet1Dto).getId();
        petService.createPet(user1Username, pet2Dto);
        petService.createPet(user1Username, pet3Dto);

        Nutrient nutrient = Nutrient.builder()
                .carbonHydrate(110)
                .fat(1.51)
                .vitamin(Vitamin.builder().vitaminA(560).vitaminD(40).vitaminE(2.1).build())
                .phosphorus(1.4)
                .calcium(9.8)
                .protein(50)
                .build();

        Pet pet = petRepository.findById(pet1Id).get();
        DailyMeal dailyMeal = new DailyMeal(nutrient, pet, 500);
        dailyMealRepository.save(dailyMeal);

        // User2(3 Pet, Membership)
        User user2 =
                User.builder().name("user2").role(Role.ADMIN)
                        .username("bbbb").password("bbbb")
                        .activated(true).isReceiveAd(true)
                        .phoneNumber("010-2222-2222")
                        .socialType(SocialType.NAVER)
                        .build();

        user2Username = userRepository.save(user2).getUsername();
        UserMemberShip user2MemberShip = new UserMemberShip(memberShip, user2);
        userMemberShipRepository.save(user2MemberShip);

        CreatePetRequestDto pet4Dto =
                CreatePetRequestDto.builder()
                        .name("pet4")
                        .age(5).weight(15).activity(Activity.ACTIVE)
                        .neutering(Neutering.INTACT)
                        .build();

        CreatePetRequestDto pet5Dto =
                CreatePetRequestDto.builder()
                        .name("pet5")
                        .age(5).weight(15).activity(Activity.SOMEWHAT_ACTIVE)
                        .neutering(Neutering.INTACT)
                        .build();

        CreatePetRequestDto pet6Dto =
                CreatePetRequestDto.builder()
                        .name("pet6")
                        .age(5).weight(15).activity(Activity.INACTIVE)
                        .neutering(Neutering.INTACT)
                        .build();

        petService.createPet(user2Username, pet4Dto);
        petService.createPet(user2Username, pet5Dto);
        petService.createPet(user2Username, pet6Dto);

        // User3(1 Pet, No Membership)
        User user3 =
                User.builder().name("user3").role(Role.ADMIN)
                        .username("cccc").password("cccc")
                        .activated(true).isReceiveAd(true)
                        .phoneNumber("010-3333-3333")
                        .socialType(SocialType.NAVER)
                        .build();

        user3Username = userRepository.save(user3).getUsername();

        CreatePetRequestDto pet7Dto =
                CreatePetRequestDto.builder()
                        .name("pet7")
                        .age(7).weight(10).activity(Activity.ACTIVE)
                        .neutering(Neutering.INTACT)
                        .build();

        petService.createPet(user3Username, pet7Dto);

        // User4 (0 Pet, No Membership)
        User user4 =
                User.builder().name("user4").role(Role.ADMIN)
                        .username("dddd").password("dddd")
                        .activated(true).isReceiveAd(true)
                        .phoneNumber("010-4444-4444")
                        .socialType(SocialType.NAVER)
                        .build();

        user4Username = userRepository.save(user4).getUsername();

        BookMarkedPackagedSnack bookMarkedPackagedSnackA = BookMarkedPackagedSnack.builder()
                .user(user1)
                .serving(200)
                .name("즐겨찾기 포장간식a")
                .kcal(100)
                .nutrient(Nutrient.builder()
                        .carbonHydrate(10)
                        .protein(10)
                        .fat(10)
                        .calcium(10)
                        .phosphorus(10)
                        .vitamin(Vitamin.builder().vitaminA(10).vitaminD(10).vitaminE(10).build())
                        .build())
                .build();
        bookMarkedPackagedSnack1Id = bookMarkedPackagedSnackRepository.save(bookMarkedPackagedSnackA).getId();

        BookMarkedPackagedSnack bookMarkedPackagedSnackB = BookMarkedPackagedSnack.builder()
                .user(user1)
                .serving(200)
                .name("즐겨찾기 포장간식b")
                .kcal(100)
                .nutrient(Nutrient.builder()
                        .carbonHydrate(10)
                        .protein(10)
                        .fat(10)
                        .calcium(10)
                        .phosphorus(10)
                        .vitamin(Vitamin.builder().vitaminA(10).vitaminD(10).vitaminE(10).build())
                        .build())
                .build();
        bookMarkedPackagedSnackRepository.save(bookMarkedPackagedSnackB);

    }

    @Test
    void createBookMarkedPackagedSnack() {
        //given
        CreateBookMarkedPackagedSnackRequestDto dto1 =
                new CreateBookMarkedPackagedSnackRequestDto(100, "즐겨찾기 포장간식1", 100,
                        10, 10, 10, 10, 10,
                        10, 10, 10);

        //when
        bookMarkedPackagedSnackService.createBookMarkedPackagedSnack(user1Username, dto1);

        //then
        Assertions.assertThrows(NotFoundException.class, ()->bookMarkedPackagedSnackService.createBookMarkedPackagedSnack("", dto1));
        Assertions.assertThrows(BadRequestException.class, ()->bookMarkedPackagedSnackService.createBookMarkedPackagedSnack(user1Username, dto1));
    }

    @Test
    void getBookMarkedPackagedSnacks() {
        // given

        // when
        List<ReadBookMarkedPackagedSnackResponseDto> bookMarkedPackagedSnacks =
                bookMarkedPackagedSnackService.getBookMarkedPackagedSnacks(user1Username);

        // then
        Assertions.assertEquals(2,bookMarkedPackagedSnacks.size());
    }

    @Test
    void getBookMarkedPackagedSnack() {

        // when
        ReadBookMarkedPackagedSnackResponseDto bookMarkedPackagedSnack =
                bookMarkedPackagedSnackService.getBookMarkedPackagedSnack(user1Username, bookMarkedPackagedSnack1Id);

        // then
        Assertions.assertNotNull(bookMarkedPackagedSnack);
        Assertions.assertEquals("즐겨찾기 포장간식a",bookMarkedPackagedSnack.getName());
    }

    @Test
    void deleteBookMarkedPackagedSnack() {

        // when
        bookMarkedPackagedSnackService.deleteBookMarkedPackagedSnack(user1Username, bookMarkedPackagedSnack1Id);

        // then
    }
}