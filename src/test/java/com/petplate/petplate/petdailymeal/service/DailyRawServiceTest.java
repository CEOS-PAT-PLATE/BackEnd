package com.petplate.petplate.petdailymeal.service;

import com.petplate.petplate.common.EmbeddedType.Nutrient;
import com.petplate.petplate.common.EmbeddedType.Vitamin;
import com.petplate.petplate.common.response.error.exception.NotFoundException;
import com.petplate.petplate.pet.domain.Activity;
import com.petplate.petplate.pet.domain.Neutering;
import com.petplate.petplate.pet.domain.entity.Pet;
import com.petplate.petplate.pet.dto.request.CreatePetRequestDto;
import com.petplate.petplate.pet.repository.PetRepository;
import com.petplate.petplate.pet.service.PetService;
import com.petplate.petplate.petdailymeal.domain.entity.DailyMeal;
import com.petplate.petplate.petdailymeal.dto.request.CreateDailyRawRequestDto;
import com.petplate.petplate.petdailymeal.dto.response.ReadDailyRawResponseDto;
import com.petplate.petplate.petdailymeal.repository.DailyMealRepository;
import com.petplate.petplate.petdailymeal.repository.DailyRawRepository;
import com.petplate.petplate.petfood.domain.entity.Raw;
import com.petplate.petplate.petfood.repository.RawRepository;
import com.petplate.petplate.petfood.service.RawService;
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

import java.util.List;

@SpringBootTest
@Transactional
class DailyRawServiceTest {

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
    private DailyRawService dailyRawService;
    @Autowired
    private RawRepository rawRepository;

    private String user1Username;
    private Long pet1Id;
    private Long pet2Id;
    private Long dailyMealId;
    private Long raw1Id;
    private Long raw2Id;
    @Autowired
    private RawService rawService;
    @Autowired
    private DailyRawRepository dailyRawRepository;


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

        // 식사내역 존재
        pet1Id = petService.createPet(user1Username, pet1Dto).getId();

        // 식사 내역 존재 안함
        pet2Id = petService.createPet(user1Username, pet2Dto).getId();

        Nutrient nutrient = Nutrient.builder()
                .carbonHydrate(1000)
                .fat(1000)
                .vitamin(Vitamin.builder().vitaminA(1000).vitaminD(1000).vitaminE(1000).build())
                .phosphorus(1000)
                .calcium(1000)
                .protein(1000)
                .build();

        Pet pet = petRepository.findById(pet1Id).get();
        DailyMeal dailyMeal = new DailyMeal(nutrient, pet, 1000);
        dailyMealId = dailyMealRepository.save(dailyMeal).getId();


        Raw raw1 = Raw.builder()
                .name("소고기")
                .standardAmount(100)
                .nutrient(Nutrient.builder()
                        .carbonHydrate(10)
                        .fat(10)
                        .vitamin(Vitamin.builder().vitaminA(10).vitaminD(10).vitaminE(10).build())
                        .phosphorus(10)
                        .calcium(10)
                        .protein(10)
                        .build())
                .kcal(10)
                .build();
        raw1Id = rawRepository.save(raw1).getId();

        Raw raw2 = Raw.builder()
                .name("닭고기")
                .nutrient(Nutrient.builder()
                        .carbonHydrate(20)
                        .fat(20)
                        .vitamin(Vitamin.builder().vitaminA(20).vitaminD(20).vitaminE(20).build())
                        .phosphorus(20)
                        .calcium(20)
                        .protein(20)
                        .build())
                .kcal(20)
                .standardAmount(100)
                .build();
        raw2Id = rawRepository.save(raw2).getId();
    }

    @Test
    @DisplayName("하루식사에 먹은 자연식 추가")
    void createDailyRaw() {
        // given

        //when
        CreateDailyRawRequestDto invalidRaw = new CreateDailyRawRequestDto(-12345L, 100);
        CreateDailyRawRequestDto 소고기 = new CreateDailyRawRequestDto(raw1Id, 100);
        CreateDailyRawRequestDto 닭고기 = new CreateDailyRawRequestDto(raw2Id, 100);

        //then
        Assertions.assertThrows(NotFoundException.class, () -> dailyRawService.createDailyRaw(user1Username, pet1Id, invalidRaw));
        dailyRawService.createDailyRaw(user1Username, pet1Id, 소고기);
        Assertions.assertEquals(1010, dailyMealRepository.findById(dailyMealId).get().getKcal());

        dailyRawService.createDailyRaw(user1Username, pet1Id, 닭고기);
        Assertions.assertEquals(1030, dailyMealRepository.findById(dailyMealId).get().getKcal());

        dailyRawService.createDailyRaw(user1Username, pet2Id, 소고기);
        Assertions.assertEquals(10, dailyMealRepository.findById(dailyMealRepository.findByPetIdOrderByCreatedAtDesc(pet2Id).get(0).getId()).get().getKcal());

        dailyRawService.createDailyRaw(user1Username, pet2Id, 닭고기);
        Assertions.assertEquals(30, dailyMealRepository.findById(dailyMealRepository.findByPetIdOrderByCreatedAtDesc(pet2Id).get(0).getId()).get().getKcal());
    }

    @Test
    @DisplayName("펫이 특정일자에 먹은 모든 자연식 리스트 조회")
    void getDailyRaws() {
        //given
        CreateDailyRawRequestDto 소고기 = new CreateDailyRawRequestDto(raw1Id, 100);
        CreateDailyRawRequestDto 닭고기 = new CreateDailyRawRequestDto(raw2Id, 100);

        //when
        dailyRawService.createDailyRaw(user1Username, pet2Id, 소고기);
        dailyRawService.createDailyRaw(user1Username, pet2Id, 닭고기);
        dailyRawService.createDailyRaw(user1Username, pet2Id, 소고기);

        Long dailyMealId = dailyMealRepository.findByPetIdOrderByCreatedAtDesc(pet2Id).get(0).getId();

        //then
        List<ReadDailyRawResponseDto> dailyRaws = dailyRawService.getDailyRaws(user1Username, pet2Id, dailyMealId);
        Assertions.assertEquals(3, dailyRaws.size());
    }

    @Test
    @DisplayName("특정 dailyRaw 조회")
    void getDailyRaw() {
        // given
        CreateDailyRawRequestDto 소고기 = new CreateDailyRawRequestDto(raw1Id, 100);

        // when
        Long dailyRawId = dailyRawService.createDailyRaw(user1Username, pet2Id, 소고기);

        // then
        dailyRawService.getDailyRaw(user1Username, pet2Id, dailyRawId);
    }

    @Test
    @DisplayName("최근 count일 동안 먹은 모든 자연식 반환")
    void getRecentDailyRaw() {
        // given
        CreateDailyRawRequestDto 소고기 = new CreateDailyRawRequestDto(raw1Id, 100);
        CreateDailyRawRequestDto 닭고기 = new CreateDailyRawRequestDto(raw2Id, 100);

        // when
        dailyRawService.createDailyRaw(user1Username, pet2Id, 소고기);
        dailyRawService.createDailyRaw(user1Username, pet2Id, 닭고기);
        dailyRawService.createDailyRaw(user1Username, pet2Id, 소고기);
        dailyRawService.createDailyRaw(user1Username, pet2Id, 닭고기);

        // then
        List<ReadDailyRawResponseDto> recentDailyRaws = dailyRawService.getRecentDailyRaws(user1Username, pet2Id, 3);
        for (ReadDailyRawResponseDto recentDailyRaw : recentDailyRaws) {
            System.out.println("recentDailyRaw = " + recentDailyRaw.getName());
        }
    }

    @Test
    @DisplayName("섭취한 자연식 내역 제거")
    void deleteDailyRaw() {
        // given
        CreateDailyRawRequestDto 소고기 = new CreateDailyRawRequestDto(raw1Id, 100);
        CreateDailyRawRequestDto 닭고기 = new CreateDailyRawRequestDto(raw2Id, 100);

        // when
        Long dailyRawId1 = dailyRawService.createDailyRaw(user1Username, pet2Id, 소고기);
        Long dailyRawId2 = dailyRawService.createDailyRaw(user1Username, pet2Id, 닭고기);

        dailyMealId = dailyMealRepository.findByPetIdOrderByCreatedAtDesc(pet2Id).get(0).getId();

        // then
        Assertions.assertEquals(30, dailyMealRepository.findById(dailyMealId).get().getKcal());
        Assertions.assertEquals(2, dailyRawRepository.findByDailyMealId(dailyMealId).size());

        dailyRawService.deleteDailyRaw(user1Username, pet2Id, dailyRawId1);
        Assertions.assertEquals(20, dailyMealRepository.findById(dailyMealId).get().getKcal());
        Assertions.assertEquals(1, dailyRawRepository.findByDailyMealId(dailyMealId).size());

        dailyRawService.deleteDailyRaw(user1Username, pet2Id, dailyRawId2);
        Assertions.assertEquals(0, dailyMealRepository.findById(dailyMealId).get().getKcal());
        Assertions.assertEquals(0, dailyRawRepository.findByDailyMealId(dailyMealId).size());

        Assertions.assertThrows(NotFoundException.class, () -> dailyRawService.deleteDailyRaw(user1Username, pet2Id, dailyRawId1));
    }
}