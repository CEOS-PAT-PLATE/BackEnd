package com.petplate.petplate.petdailymeal.service;

import com.petplate.petplate.common.EmbeddedType.Nutrient;
import com.petplate.petplate.common.EmbeddedType.Vitamin;
import com.petplate.petplate.pet.domain.Activity;
import com.petplate.petplate.pet.domain.Neutering;
import com.petplate.petplate.pet.domain.entity.Pet;
import com.petplate.petplate.pet.dto.request.CreatePetRequestDto;
import com.petplate.petplate.pet.repository.PetRepository;
import com.petplate.petplate.pet.service.PetService;
import com.petplate.petplate.petdailymeal.domain.entity.DailyMeal;
import com.petplate.petplate.petdailymeal.repository.DailyMealRepository;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class DailyMealServiceTest {
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
    private DailyMealRepository dailyMealRepository;
    @Autowired
    private DailyMealService dailyMealService;

    private String user1Username;
    private Long pet1Id;
    private Long pet2Id;
    private Long dailyMealId;


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
        pet2Id = petService.createPet(user1Username, pet2Dto).getId();
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
        dailyMealId = dailyMealRepository.save(dailyMeal).getId();
    }

    @Test
    @DisplayName("하루식사 생성")
    void createDailyMeal() {
        Long pet1DailyMealId = dailyMealService.createDailyMeal(user1Username, pet1Id).getId();
        Assertions.assertEquals(pet1DailyMealId, dailyMealId);

        Long pet2DailyMealId
                = dailyMealService.createDailyMeal(user1Username, pet2Id).getId();
        Assertions.assertNotEquals(dailyMealId, pet2DailyMealId);

         Long pet2DailyMealId2
                = dailyMealService.createDailyMeal(user1Username, pet2Id).getId();
        Assertions.assertEquals(pet2DailyMealId, pet2DailyMealId2);
    }
}