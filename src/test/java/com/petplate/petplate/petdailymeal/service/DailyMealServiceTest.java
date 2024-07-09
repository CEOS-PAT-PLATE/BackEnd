package com.petplate.petplate.petdailymeal.service;

import com.petplate.petplate.common.EmbeddedType.Nutrient;
import com.petplate.petplate.common.EmbeddedType.Vitamin;
import com.petplate.petplate.common.response.error.exception.BadRequestException;
import com.petplate.petplate.common.response.error.exception.NotFoundException;
import com.petplate.petplate.dailyMealNutrient.service.DeficientNutrientService;
import com.petplate.petplate.dailyMealNutrient.service.ProperNutrientService;
import com.petplate.petplate.dailyMealNutrient.service.SufficientNutrientService;
import com.petplate.petplate.pet.domain.Activity;
import com.petplate.petplate.pet.domain.Neutering;
import com.petplate.petplate.pet.domain.entity.Pet;
import com.petplate.petplate.pet.dto.request.CreatePetRequestDto;
import com.petplate.petplate.pet.dto.response.ReadPetNutrientResponseDto;
import com.petplate.petplate.pet.repository.PetRepository;
import com.petplate.petplate.pet.service.PetService;
import com.petplate.petplate.petdailymeal.domain.entity.*;
import com.petplate.petplate.petdailymeal.dto.response.*;
import com.petplate.petplate.petdailymeal.repository.*;
import com.petplate.petplate.petfood.domain.entity.BookMarkedRaw;
import com.petplate.petplate.petfood.domain.entity.Raw;
import com.petplate.petplate.petfood.repository.BookMarkedRawRepository;
import com.petplate.petplate.petfood.repository.RawRepository;
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

import java.time.LocalDate;
import java.util.List;

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
    @Autowired
    private RawRepository rawRepository;
    @Autowired
    private BookMarkedRawRepository bookMarkedRawRepository;
    @Autowired
    private DailyFeedRepository dailyFeedRepository;
    @Autowired
    private DailyBookMarkedFeedRepository dailyBookMarkedFeedRepository;
    @Autowired
    private DailyRawRepository dailyRawRepository;
    @Autowired
    private DailyBookMarkedRawRepository dailyBookMarkedRawRepository;
    @Autowired
    DeficientNutrientService deficientNutrientService;
    @Autowired
    ProperNutrientService properNutrientService;
    @Autowired
    SufficientNutrientService sufficientNutrientService;

    private String user1Username;
    private Long pet1Id;
    private Long pet2Id;
    private Long dailyMealId;


    @BeforeEach
    public void each() {
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

        Pet pet1 = petRepository.findById(pet1Id).get();
        DailyMeal dailyMeal = new DailyMeal(nutrient, pet1, 500);
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

    @Test
    @DisplayName("날짜로 하루식사 조회")
    public void getDailyMealByDate() throws Exception {
        //given

        //when
        DailyMeal dailyMealByDate = dailyMealService.getDailyMealByDate(user1Username, pet1Id, LocalDate.now());

        //then
        Assertions.assertEquals(dailyMealId, dailyMealByDate.getId());
        Assertions.assertThrows(NotFoundException.class, () -> dailyMealService.getDailyMealByDate(user1Username, pet1Id, LocalDate.of(1, 1, 1)));

        Assertions.assertThrows(NotFoundException.class, () -> dailyMealService.getDailyMealByDate(user1Username, -12345L, LocalDate.now()));
        Assertions.assertThrows(BadRequestException.class, () -> dailyMealService.getDailyMealByDate("", pet1Id, LocalDate.now()));
    }

    @Test
    @DisplayName("모든 하루식사 조회")
    public void getDailyMeals() throws Exception {
        //given
        DailyMeal dailyMeal = dailyMealService.createDailyMeal(user1Username, pet2Id);

        DailyFeed dailyFeed1 = DailyFeed.builder()
                .name("dailyFeed1")
                .kcal(100)
                .serving(100)
                .nutrient(Nutrient.builder()
                        .carbonHydrate(100)
                        .fat(100)
                        .phosphorus(100)
                        .calcium(100)
                        .protein(100)
                        .vitamin(Vitamin.builder().vitaminA(100).vitaminD(100).vitaminE(100).build())
                        .build())
                .dailyMeal(dailyMeal)
                .build();
        DailyFeed dailyFeed2 = DailyFeed.builder()
                .name("dailyFeed2")
                .kcal(200)
                .serving(200)
                .nutrient(Nutrient.builder()
                        .carbonHydrate(200)
                        .fat(200)
                        .phosphorus(200)
                        .calcium(200)
                        .protein(200)
                        .vitamin(Vitamin.builder().vitaminA(200).vitaminD(200).vitaminE(200).build())
                        .build())
                .dailyMeal(dailyMeal)
                .build();
        DailyFeed dailyFeed3 = DailyFeed.builder()
                .name("dailyFeed3")
                .kcal(300)
                .serving(300)
                .nutrient(Nutrient.builder()
                        .carbonHydrate(300)
                        .fat(300)
                        .phosphorus(300)
                        .calcium(300)
                        .protein(300)
                        .vitamin(Vitamin.builder().vitaminA(300).vitaminD(300).vitaminE(300).build())
                        .build())
                .dailyMeal(dailyMeal)
                .build();
        dailyFeedRepository.save(dailyFeed1);
        dailyFeedRepository.save(dailyFeed2);
        dailyFeedRepository.save(dailyFeed3);

        Raw raw1 = Raw.builder()
                .name("raw1")
                .description("raw1 description")
                .kcal(400)
                .standardAmount(400)
                .nutrient(Nutrient.builder()
                        .carbonHydrate(400)
                        .fat(400)
                        .phosphorus(400)
                        .calcium(400)
                        .protein(400)
                        .vitamin(Vitamin.builder().vitaminA(400).vitaminD(400).vitaminE(400).build())
                        .build())
                .build();

        Raw raw2 = Raw.builder()
                .name("raw2")
                .description("raw2 description")
                .kcal(500)
                .standardAmount(500)
                .nutrient(Nutrient.builder()
                        .carbonHydrate(500)
                        .fat(500)
                        .phosphorus(500)
                        .calcium(500)
                        .protein(500)
                        .vitamin(Vitamin.builder().vitaminA(500).vitaminD(500).vitaminE(500).build())
                        .build())
                .build();
        rawRepository.save(raw1);
        rawRepository.save(raw2);

        DailyRaw dailyRaw = DailyRaw.builder()
                .raw(raw1)
                .serving(400)
                .dailyMeal(dailyMeal)
                .build();
        dailyRawRepository.save(dailyRaw);

        BookMarkedRaw bookMarkedRaw = BookMarkedRaw.builder()
                .raw(raw2)
                .serving(500)
                .user(userRepository.findByUsername(user1Username).get())
                .build();
        bookMarkedRawRepository.save(bookMarkedRaw);

        DailyBookMarkedRaw dailyBookMarkedRaw = DailyBookMarkedRaw.builder()
                .bookMarkedRaw(bookMarkedRaw)
                .dailyMeal(dailyMeal)
                .build();
        dailyBookMarkedRawRepository.save(dailyBookMarkedRaw);

        //when
        List<ReadDailyMealResponseDto> dailyMeals = dailyMealService.getDailyMeals(user1Username, pet1Id);

        //then
        Assertions.assertEquals(1, dailyMeals.size());
        Assertions.assertEquals(3, dailyFeedRepository.findByDailyMealId(dailyMeal.getId()).size());
        Assertions.assertEquals(1, dailyRawRepository.findByDailyMealId(dailyMeal.getId()).size());
        Assertions.assertEquals(1, dailyBookMarkedRawRepository.findByDailyMealId(dailyMeal.getId()).size());
        Assertions.assertEquals(raw2.getId(), dailyBookMarkedRawRepository.findByDailyMealId(dailyMeal.getId()).get(0).getBookMarkedRaw().getRaw().getId());
        Assertions.assertEquals(LocalDate.now(), dailyMeals.get(0).getDate());
    }

    @Test
    @DisplayName("하루식사에 섭취한 음식들 조회")
    public void getDailyFoods() throws Exception {
        //given
        DailyMeal dailyMeal = dailyMealService.createDailyMeal(user1Username, pet2Id);

        // dailyRaw 2개
        Raw raw1 = Raw.builder()
                .name("raw1")
                .description("raw1 description")
                .kcal(400)
                .standardAmount(400)
                .nutrient(Nutrient.builder()
                        .carbonHydrate(400)
                        .fat(400)
                        .phosphorus(400)
                        .calcium(400)
                        .protein(400)
                        .vitamin(Vitamin.builder().vitaminA(400).vitaminD(400).vitaminE(400).build())
                        .build())
                .build();

        Raw raw2 = Raw.builder()
                .name("raw2")
                .description("raw2 description")
                .kcal(500)
                .standardAmount(500)
                .nutrient(Nutrient.builder()
                        .carbonHydrate(500)
                        .fat(500)
                        .phosphorus(500)
                        .calcium(500)
                        .protein(500)
                        .vitamin(Vitamin.builder().vitaminA(500).vitaminD(500).vitaminE(500).build())
                        .build())
                .build();
        rawRepository.save(raw1);
        rawRepository.save(raw2);

        DailyRaw dailyRaw1 = DailyRaw.builder()
                .raw(raw1)
                .serving(400)
                .dailyMeal(dailyMeal)
                .build();
        dailyRawRepository.save(dailyRaw1);

        DailyRaw dailyRaw2 = DailyRaw.builder()
                .raw(raw2)
                .serving(500)
                .dailyMeal(dailyMeal)
                .build();
        dailyRawRepository.save(dailyRaw2);

        // dailyBookMarkedRaw 1개
        BookMarkedRaw bookMarkedRaw = BookMarkedRaw.builder()
                .raw(raw2)
                .serving(500)
                .user(userRepository.findByUsername(user1Username).get())
                .build();
        bookMarkedRawRepository.save(bookMarkedRaw);

        DailyBookMarkedRaw dailyBookMarkedRaw = DailyBookMarkedRaw.builder()
                .dailyMeal(dailyMeal)
                .bookMarkedRaw(bookMarkedRaw)
                .build();
        dailyBookMarkedRawRepository.save(dailyBookMarkedRaw);

        // dailyFeed 3개
        DailyFeed dailyFeed1 = DailyFeed.builder()
                .name("dailyFeed1")
                .kcal(100)
                .serving(100)
                .nutrient(Nutrient.builder()
                        .carbonHydrate(100)
                        .fat(100)
                        .phosphorus(100)
                        .calcium(100)
                        .protein(100)
                        .vitamin(Vitamin.builder().vitaminA(100).vitaminD(100).vitaminE(100).build())
                        .build())
                .dailyMeal(dailyMeal)
                .build();
        DailyFeed dailyFeed2 = DailyFeed.builder()
                .name("dailyFeed2")
                .kcal(200)
                .serving(200)
                .nutrient(Nutrient.builder()
                        .carbonHydrate(200)
                        .fat(200)
                        .phosphorus(200)
                        .calcium(200)
                        .protein(200)
                        .vitamin(Vitamin.builder().vitaminA(200).vitaminD(200).vitaminE(200).build())
                        .build())
                .dailyMeal(dailyMeal)
                .build();
        DailyFeed dailyFeed3 = DailyFeed.builder()
                .name("dailyFeed3")
                .kcal(300)
                .serving(300)
                .nutrient(Nutrient.builder()
                        .carbonHydrate(300)
                        .fat(300)
                        .phosphorus(300)
                        .calcium(300)
                        .protein(300)
                        .vitamin(Vitamin.builder().vitaminA(300).vitaminD(300).vitaminE(300).build())
                        .build())
                .dailyMeal(dailyMeal)
                .build();
        dailyFeedRepository.save(dailyFeed1);
        dailyFeedRepository.save(dailyFeed2);
        dailyFeedRepository.save(dailyFeed3);

        //when
        List<ReadDailyRawResponseDto> dailyRaws = dailyMealService.getDailyRaws(user1Username, pet2Id, dailyMeal.getId());
        List<ReadDailyBookMarkedRawResponseDto> dailyBookMarkedRaws = dailyMealService.getDailyBookMarkedRaws(user1Username, pet2Id, dailyMeal.getId());
        List<ReadDailyFeedResponseDto> dailyFeeds = dailyMealService.getDailyFeeds(user1Username, pet2Id, dailyMeal.getId());
        ReadDailyMealFoodResponseDto dailyMealWithFoods = dailyMealService.getDailyMealWithFoods(user1Username, pet2Id, dailyMeal.getId());

        //then
        Assertions.assertEquals(dailyRaws.size(), 2);
        Assertions.assertEquals(dailyBookMarkedRaws.size(), 1);
        Assertions.assertEquals(dailyFeeds.size(),3);

        Assertions.assertTrue(dailyMealService.getDailyPackagedSnacks(user1Username, pet2Id, dailyMeal.getId()).isEmpty());
        Assertions.assertTrue(dailyMealService.getDailyBookMarkedFeeds(user1Username, pet2Id, dailyMeal.getId()).isEmpty());
        Assertions.assertTrue(dailyMealService.getDailyBookMarkedPackagedSnacks(user1Username, pet2Id, dailyMeal.getId()).isEmpty());

        Assertions.assertEquals(dailyMealWithFoods.getDailyRaws().size(), 2);
        Assertions.assertEquals(dailyMealWithFoods.getDailyBookMarkedRaws().size(), 1);
        Assertions.assertEquals(dailyMealWithFoods.getDailyFeeds().size(),3);

        Assertions.assertTrue(dailyMealWithFoods.getDailyPackagedSnacks().isEmpty());
        Assertions.assertTrue(dailyMealWithFoods.getDailyBookMarkedFeeds().isEmpty());
        Assertions.assertTrue(dailyMealWithFoods.getDailyBookMarkedPackagedSnacks().isEmpty());

        Assertions.assertThrows(NotFoundException.class, () -> dailyMealService.getDailyMealWithFoods(user1Username, pet2Id, -12345L));
    }

    @Test
    @DisplayName("영양분석")
    public void dailyMealNutrient() throws Exception{
        //given

        //when
        deficientNutrientService.createDeficientNutrientToday(user1Username, pet1Id);
        sufficientNutrientService.createSufficientNutrientsToday(user1Username, pet1Id);
        properNutrientService.createProperNutrientsToday(user1Username, pet1Id);

        // then
        List<ReadPetNutrientResponseDto> deficientNutrients = deficientNutrientService.getDeficientNutrients(user1Username, pet1Id, dailyMealId);
        System.out.println("deficientNutrients = " + deficientNutrients);

        List<ReadPetNutrientResponseDto> sufficientNutrients = sufficientNutrientService.getSufficientNutrients(user1Username, pet1Id, dailyMealId);
        System.out.println("sufficientNutrients = " + sufficientNutrients);

        List<ReadPetNutrientResponseDto> properNutrients = properNutrientService.getProperNutrients(user1Username, pet1Id, dailyMealId);
        System.out.println("properNutrients = " + properNutrients);

        Assertions.assertThrows(NotFoundException.class, () -> deficientNutrientService.createDeficientNutrientToday(user1Username, pet2Id));
        Assertions.assertThrows(NotFoundException.class, () -> properNutrientService.createProperNutrientsToday(user1Username, pet2Id));
        System.out.println(Assertions.assertThrows(NotFoundException.class, () -> sufficientNutrientService.createSufficientNutrientsToday(user1Username, pet2Id)).getMessage());

        Assertions.assertThrows(BadRequestException.class, () -> deficientNutrientService.createDeficientNutrientToday(user1Username, pet1Id));
        Assertions.assertThrows(BadRequestException.class, () -> sufficientNutrientService.createSufficientNutrientsToday(user1Username, pet1Id));
        System.out.println(Assertions.assertThrows(BadRequestException.class, () -> properNutrientService.createProperNutrientsToday(user1Username, pet1Id)).getMessage());
    }
}