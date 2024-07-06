package com.petplate.petplate.petdailymeal.service;

import com.petplate.petplate.common.EmbeddedType.Nutrient;
import com.petplate.petplate.common.EmbeddedType.Vitamin;
import com.petplate.petplate.common.response.error.exception.BadRequestException;
import com.petplate.petplate.common.response.error.exception.NotFoundException;
import com.petplate.petplate.pet.domain.Activity;
import com.petplate.petplate.pet.domain.Neutering;
import com.petplate.petplate.pet.domain.entity.Pet;
import com.petplate.petplate.pet.dto.request.CreatePetRequestDto;
import com.petplate.petplate.pet.repository.PetRepository;
import com.petplate.petplate.pet.service.PetService;
import com.petplate.petplate.petdailymeal.domain.entity.DailyFeed;
import com.petplate.petplate.petdailymeal.domain.entity.DailyMeal;
import com.petplate.petplate.petdailymeal.dto.request.CreateDailyFeedRequestDto;
import com.petplate.petplate.petdailymeal.dto.response.ReadDailyFeedResponseDto;
import com.petplate.petplate.petdailymeal.repository.DailyFeedRepository;
import com.petplate.petplate.petdailymeal.repository.DailyMealRepository;
import com.petplate.petplate.user.domain.Role;
import com.petplate.petplate.user.domain.SocialType;
import com.petplate.petplate.user.domain.entity.MemberShip;
import com.petplate.petplate.user.domain.entity.User;
import com.petplate.petplate.user.domain.entity.UserMemberShip;
import com.petplate.petplate.user.repository.MemberShipRepository;
import com.petplate.petplate.user.repository.UserMemberShipRepository;
import com.petplate.petplate.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@Transactional
class DailyDailyFeedServiceTest {

    @Autowired
    DailyFeedService dailyFeedService;
    @Autowired
    DailyFeedRepository dailyFeedRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PetRepository petRepository;
    @Autowired
    PetService petService;
    @Autowired
    UserMemberShipRepository userMemberShipRepository;
    @Autowired
    MemberShipRepository memberShipRepository;
    @Autowired
    DailyMealRepository dailyMealRepository;
    @Autowired
    DailyMealService dailyMealService;

    private String user1Username;
    private Long pet1Id;
    private Long pet2Id;
    private Long dailyMealId;

    @BeforeEach
    public void each() {
        // MemberShip
        MemberShip memberShip = new MemberShip("1년짜리 멤버십", 10000, 365);

        memberShipRepository.save(memberShip);

        // User1(2 Pet, Membership)
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
                        .neutering(Neutering.INTACT)
                        .build();

        CreatePetRequestDto pet2Dto =
                CreatePetRequestDto.builder()
                        .name("pet2")
                        .age(3).weight(5).activity(Activity.SOMEWHAT_ACTIVE)
                        .neutering(Neutering.INTACT)
                        .build();

        // 식사내역 존재
        pet1Id = petService.createPet(user1Username, pet1Dto).getId();

        // 식사 내역 존재 안함
        pet2Id = petService.createPet(user1Username, pet2Dto).getId();

        Nutrient nutrient = Nutrient.builder()
                .carbonHydrate(0)
                .fat(0)
                .vitamin(Vitamin.builder().vitaminA(0).vitaminD(0).vitaminE(0).build())
                .phosphorus(0)
                .calcium(0)
                .protein(0)
                .build();

        Pet pet = petRepository.findById(pet1Id).get();
        DailyMeal dailyMeal = new DailyMeal(nutrient, pet, 0);
        dailyMealId = dailyMealRepository.save(dailyMeal).getId();
    }

    @Test
    @DisplayName("Feed 생성")
    void createFeed() {
        // given
        CreateDailyFeedRequestDto 듀먼
                = new CreateDailyFeedRequestDto(50, "듀먼", 70, 0, 10.1, 5.9, 0.2, 0.2, 0.001, 0.001, 0.001);

        // when
        Long id1 = dailyFeedService.createDailyFeed(user1Username, pet1Id, 듀먼);

        DailyFeed dailyFeed1 =
                dailyFeedRepository.findById(id1).get();
        DailyMeal dailyMeal1 = dailyMealRepository.findById(dailyMealId).get();


        Long id2 = dailyFeedService.createDailyFeed(user1Username, pet2Id, 듀먼);
        Long id3 = dailyFeedService.createDailyFeed(user1Username, pet2Id, 듀먼);

        DailyFeed dailyFeed2 = dailyFeedRepository.findById(id2).get();
        DailyFeed dailyFeed3 = dailyFeedRepository.findById(id3).get();

        DailyMeal dailyMeal2 = dailyMealService.getDailyMealByDate(user1Username, pet2Id, LocalDate.now());


        // then
        Assertions.assertEquals(dailyFeed1.getKcal(), dailyMeal1.getKcal());
        Assertions.assertEquals(dailyFeed1.getNutrient().getCalcium(), dailyMeal1.getNutrient().getCalcium());

        Assertions.assertEquals(dailyMeal2.getKcal(), dailyFeed2.getKcal() + dailyFeed3.getKcal());
        Assertions.assertEquals(dailyMeal2.getNutrient().getVitamin().getVitaminD(), dailyFeed2.getNutrient().getVitamin().getVitaminD() + dailyFeed3.getNutrient().getVitamin().getVitaminD());
        Assertions.assertEquals(듀먼.getName(), dailyFeed1.getName());

        Assertions.assertThrows(NotFoundException.class, () -> dailyFeedService.createDailyFeed(user1Username, -123456L, 듀먼));
        Assertions.assertThrows(BadRequestException.class, () -> dailyFeedService.createDailyFeed("", pet1Id, 듀먼));
    }

    @Test
    @DisplayName("Feed 조회")
    void getFeed() {
        // given
        CreateDailyFeedRequestDto 듀먼
                = new CreateDailyFeedRequestDto(50, "듀먼", 70, 0, 10.1, 5.9, 0.2, 0.2, 0.001, 0.001, 0.001);
        CreateDailyFeedRequestDto 썬키스트
                = new CreateDailyFeedRequestDto(150, "썬키스트", 300, 0, 12.1, 4.9, 0.5, 0.3, 0.001, 0.001, 0.001);

        Long id1 = dailyFeedService.createDailyFeed(user1Username, pet1Id, 듀먼);
        Long id2 = dailyFeedService.createDailyFeed(user1Username, pet2Id, 썬키스트);

        // when
        ReadDailyFeedResponseDto dailyFeed1 = dailyFeedService.getDailyFeed(user1Username, pet1Id, id1);
        ReadDailyFeedResponseDto dailyFeed2 = dailyFeedService.getDailyFeed(user1Username, pet2Id, id2);

        // then
        Assertions.assertThrows(NotFoundException.class, () -> dailyFeedService.getDailyFeed(user1Username, -123456L, dailyFeed1.getDailyFeedId()));
        Assertions.assertThrows(BadRequestException.class, () -> dailyFeedService.getDailyFeed("", pet1Id, dailyFeed1.getDailyFeedId()));
        Assertions.assertThrows(NotFoundException.class, () -> dailyFeedService.getDailyFeed(user1Username, pet1Id, -123456L));
        Assertions.assertThrows(BadRequestException.class, () -> dailyFeedService.getDailyFeed(user1Username, pet1Id, dailyFeed2.getDailyFeedId()));

        Assertions.assertEquals(id1, dailyFeed1.getDailyFeedId());
        Assertions.assertEquals(썬키스트.getName(), dailyFeed2.getName());
    }

    @Test
    @DisplayName("Feed 제거")
    void deleteFeed() {
        // given
        CreateDailyFeedRequestDto 듀먼
                = new CreateDailyFeedRequestDto(50, "듀먼", 70, 0, 10.1, 5.9, 0.2, 0.2, 0.001, 0.001, 0.001);
        CreateDailyFeedRequestDto 썬키스트
                = new CreateDailyFeedRequestDto(150, "썬키스트", 300, 0, 12.1, 4.9, 0.5, 0.3, 0.001, 0.001, 0.001);

        Long pet1FeedId1 = dailyFeedService.createDailyFeed(user1Username, pet1Id, 듀먼);
        Long pet1FeedId2 = dailyFeedService.createDailyFeed(user1Username, pet1Id, 썬키스트);
        Long pet2FeedId1 = dailyFeedService.createDailyFeed(user1Username, pet2Id, 듀먼);
        Long pet2FeedId2 = dailyFeedService.createDailyFeed(user1Username, pet2Id, 썬키스트);

        // when
        dailyFeedService.deleteDailyFeed(user1Username, pet1Id, pet1FeedId1);
        dailyFeedService.deleteDailyFeed(user1Username, pet2Id, pet2FeedId1);

        //then
        DailyMeal pet2DailyMeal = dailyMealService.getDailyMealByDate(user1Username, pet2Id, LocalDate.now());
        List<DailyFeed> pet2DailyFeeds = dailyFeedRepository.findByDailyMealId(pet2DailyMeal.getId());

        Assertions.assertEquals(1, pet2DailyFeeds.size());
        Assertions.assertEquals(썬키스트.getName(), pet2DailyFeeds.get(0).getName());
        Assertions.assertEquals(dailyFeedRepository.findById(pet2FeedId2).get().getNutrient().getCalcium(), pet2DailyMeal.getNutrient().getCalcium());


        Assertions.assertThrows(BadRequestException.class, () -> dailyFeedService.deleteDailyFeed("", pet1Id, pet1FeedId2)).printStackTrace();
        Assertions.assertThrows(NotFoundException.class, () -> dailyFeedService.deleteDailyFeed(user1Username, -12345L, pet1FeedId2)).printStackTrace();
        Assertions.assertThrows(NotFoundException.class, () -> dailyFeedService.deleteDailyFeed(user1Username, pet1Id, -12345L)).printStackTrace();
        Assertions.assertThrows(NotFoundException.class, () -> dailyFeedService.deleteDailyFeed(user1Username, pet1Id, pet1FeedId1)).printStackTrace();
        Assertions.assertThrows(BadRequestException.class, () -> dailyFeedService.deleteDailyFeed(user1Username, pet1Id, pet2FeedId2)).printStackTrace();
    }
}