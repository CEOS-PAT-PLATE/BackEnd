package com.petplate.petplate.petfood.service;

import com.petplate.petplate.common.EmbeddedType.Nutrient;
import com.petplate.petplate.common.EmbeddedType.Vitamin;
import com.petplate.petplate.common.response.error.exception.BadRequestException;
import com.petplate.petplate.common.response.error.exception.NotFoundException;
import com.petplate.petplate.pet.domain.Activity;
import com.petplate.petplate.pet.domain.Neutering;
import com.petplate.petplate.pet.domain.entity.Pet;
import com.petplate.petplate.pet.repository.PetRepository;
import com.petplate.petplate.petdailymeal.domain.entity.DailyBookMarkedRaw;
import com.petplate.petplate.petdailymeal.domain.entity.DailyMeal;
import com.petplate.petplate.petdailymeal.domain.entity.DailyRaw;
import com.petplate.petplate.petdailymeal.repository.DailyBookMarkedRawRepository;
import com.petplate.petplate.petdailymeal.repository.DailyMealRepository;
import com.petplate.petplate.petdailymeal.repository.DailyRawRepository;
import com.petplate.petplate.petfood.domain.entity.BookMarkedRaw;
import com.petplate.petplate.petfood.domain.entity.Raw;
import com.petplate.petplate.petfood.dto.request.CreateRawRequestDto;
import com.petplate.petplate.petfood.dto.response.ReadRawResponseDto;
import com.petplate.petplate.petfood.repository.BookMarkedRawRepository;
import com.petplate.petplate.petfood.repository.RawRepository;
import com.petplate.petplate.user.domain.Role;
import com.petplate.petplate.user.domain.SocialType;
import com.petplate.petplate.user.domain.entity.User;
import com.petplate.petplate.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Transactional
class RawServiceTest {
    @Autowired
    private RawService rawService;
    @Autowired
    private RawRepository rawRepository;

    private Long appleId;
    private Long bananaId;
    private Long beefId;
    private Long porkId;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PetRepository petRepository;
    @Autowired
    private DailyMealRepository dailyMealRepository;
    @Autowired
    private DailyRawRepository dailyRawRepository;
    @Autowired
    private BookMarkedRawRepository bookMarkedRawRepository;
    @Autowired
    private DailyBookMarkedRawRepository dailyBookMarkedRawRepository;

    @BeforeEach
    void setUp() {
        // apple, banana: description 존재
        // beef, pork: description 미 존재

        Raw apple = Raw.builder()
                .name("사과")
                .standardAmount(100)
                .description("1개")
                .kcal(30)
                .nutrient(Nutrient.builder()
                        .carbonHydrate(10)
                        .protein(10)
                        .fat(10)
                        .calcium(10)
                        .phosphorus(10)
                        .vitamin(Vitamin.builder().vitaminA(10).vitaminD(10).vitaminE(10).build())
                        .build())
                .build();
        Raw banana = Raw.builder()
                .name("바나나")
                .standardAmount(100)
                .description("1개 (18~20cm)")
                .kcal(30)
                .nutrient(Nutrient.builder()
                        .carbonHydrate(10)
                        .protein(10)
                        .fat(10)
                        .calcium(10)
                        .phosphorus(10)
                        .vitamin(Vitamin.builder().vitaminA(10).vitaminD(10).vitaminE(10).build())
                        .build())
                .build();
        Raw beef = Raw.builder()
                .name("소고기")
                .standardAmount(100)
                .kcal(30)
                .nutrient(Nutrient.builder()
                        .carbonHydrate(10)
                        .protein(10)
                        .fat(10)
                        .calcium(10)
                        .phosphorus(10)
                        .vitamin(Vitamin.builder().vitaminA(10).vitaminD(10).vitaminE(10).build())
                        .build())
                .build();
        Raw pork = Raw.builder()
                .name("돼지고기")
                .standardAmount(100)
                .kcal(30)
                .nutrient(Nutrient.builder()
                        .carbonHydrate(10)
                        .protein(10)
                        .fat(10)
                        .calcium(10)
                        .phosphorus(10)
                        .vitamin(Vitamin.builder().vitaminA(10).vitaminD(10).vitaminE(10).build())
                        .build())
                .build();

        appleId = rawRepository.save(apple).getId();
        bananaId = rawRepository.save(banana).getId();
        beefId = rawRepository.save(beef).getId();
        porkId = rawRepository.save(pork).getId();
    }

    @Test
    @DisplayName("자연식 생성")
    void createRaw() {

        // when
        CreateRawRequestDto createApple =
                CreateRawRequestDto.builder()
                        .name("사과")
                        .standardAmount(100)
                        .description("1개")
                        .kcal(30)
                        .carbonHydrate(10)
                        .protein(10)
                        .fat(10)
                        .calcium(10)
                        .phosphorus(10)
                        .vitaminA(10)
                        .vitaminD(10)
                        .vitaminE(10)
                        .build();

        CreateRawRequestDto createChicken =
                CreateRawRequestDto.builder()
                        .name("닭고기")
                        .standardAmount(100)
                        .description("1개")
                        .kcal(30)
                        .carbonHydrate(10)
                        .protein(10)
                        .fat(10)
                        .calcium(10)
                        .phosphorus(10)
                        .vitaminA(10)
                        .vitaminD(10)
                        .vitaminE(10)
                        .build();

        // then
        Assertions.assertThrows(BadRequestException.class, () -> rawService.createRaw(createApple)); // 자연식 이름 중복
        Assertions.assertNotNull(rawService.createRaw(createChicken));
    }

    @Test
    @DisplayName("id로 자연식 조회")
    void getRaw() {
        // given

        // when
        ReadRawResponseDto apple = rawService.getRaw(appleId);
        ReadRawResponseDto beef = rawService.getRaw(beefId);

        // then
        Assertions.assertEquals(appleId, apple.getRawId());
        Assertions.assertEquals("사과", apple.getName());
        Assertions.assertEquals("1개", apple.getDescription());

        Assertions.assertEquals(beefId, beef.getRawId());
        Assertions.assertEquals("소고기", beef.getName());
        Assertions.assertNull(beef.getDescription());

        Assertions.assertThrows(NotFoundException.class, () -> rawService.getRaw(-12345L));
    }

    @Test
    @DisplayName("키워드가 포함된 자연식 조회")
    void getRawByKeyword() {
        // given
        Raw tomato1 = Raw.builder()
                .name("토마토주스")
                .standardAmount(100)
                .description("1개")
                .kcal(30)
                .nutrient(Nutrient.builder()
                        .carbonHydrate(10)
                        .protein(10)
                        .fat(10)
                        .calcium(10)
                        .phosphorus(10)
                        .vitamin(Vitamin.builder().vitaminA(10).vitaminD(10).vitaminE(10).build())
                        .build())
                .build();
        Raw tomato2 = Raw.builder()
                .name("얼린토마토")
                .standardAmount(100)
                .description("1개")
                .kcal(30)
                .nutrient(Nutrient.builder()
                        .carbonHydrate(10)
                        .protein(10)
                        .fat(10)
                        .calcium(10)
                        .phosphorus(10)
                        .vitamin(Vitamin.builder().vitaminA(10).vitaminD(10).vitaminE(10).build())
                        .build())
                .build();
        Raw tomato3 = Raw.builder()
                .name("맛있는토마토주스")
                .standardAmount(100)
                .description("1개")
                .kcal(30)
                .nutrient(Nutrient.builder()
                        .carbonHydrate(10)
                        .protein(10)
                        .fat(10)
                        .calcium(10)
                        .phosphorus(10)
                        .vitamin(Vitamin.builder().vitaminA(10).vitaminD(10).vitaminE(10).build())
                        .build())
                .build();
        rawRepository.save(tomato1);
        rawRepository.save(tomato2);
        rawRepository.save(tomato3);

        // when
        List<ReadRawResponseDto> tomato = rawService.getRawByKeyword("토마토");

        // then
        Assertions.assertEquals(3, tomato.size());
        for (ReadRawResponseDto readRawResponseDto : tomato) {
            System.out.println("readRawResponseDto.getName() = " + readRawResponseDto.getName());
        }
    }

    @Test
    @DisplayName("이름으로 자연식 조회")
    void getRawByName() {

        // given

        // when
        ReadRawResponseDto apple = rawService.getRawByName("사과");

        // then
        Assertions.assertEquals(appleId, apple.getRawId());
        Assertions.assertEquals("사과", apple.getName());
        Assertions.assertThrows(NotFoundException.class, () -> rawService.getRawByName("없는 음식"));
    }

    @Test
    @DisplayName("자연식 제거")
    void deleteRawById() {
        // given
        // DB에 미리 pk가 -1이고 name이 "존재하지 않는 정보입니다"이고, 영양 정보가 0인 Raw가 준비 되어 있어야 함.

        User user = User.builder()
                .name("test")
                .activated(true)
                .role(Role.ADMIN)
                .username("asdf")
                .password("asdf")
                .isReceiveAd(true)
                .phoneNumber("123")
                .socialType(SocialType.NAVER)
                .build();
        userRepository.save(user);

        Pet pet = Pet.builder()
                .owner(user)
                .name("test pet")
                .age(11)
                .weight(10)
                .activity(Activity.ACTIVE)
                .neutering(Neutering.NEUTERED)
                .build();
        petRepository.save(pet);

        // DailyRaw + DailyBookMarkedRaw
        DailyMeal dailyMeal = DailyMeal.builder()
                .pet(pet)
                .kcal(60)
                .nutrient(
                        Nutrient.builder()
                                .carbonHydrate(20)
                                .protein(20)
                                .fat(20)
                                .calcium(20)
                                .phosphorus(20)
                                .vitamin(Vitamin.builder().vitaminA(20).vitaminD(20).vitaminE(20).build())
                                .build())
                .build();
        Long dailyMealId = dailyMealRepository.save(dailyMeal).getId();

        Raw apple = rawRepository.findById(appleId).get();

        DailyRaw dailyRaw = DailyRaw.builder()
                .raw(apple)
                .dailyMeal(dailyMeal)
                .serving(100)
                .build();
        Long dailyRawId = dailyRawRepository.save(dailyRaw).getId();

        BookMarkedRaw bookMarkedRaw = BookMarkedRaw.builder()
                .user(user)
                .raw(apple)
                .serving(100)
                .build();
        Long bookMarkedRawId = bookMarkedRawRepository.save(bookMarkedRaw).getId();

        DailyBookMarkedRaw dailyBookMarkedRaw = DailyBookMarkedRaw.builder()
                .bookMarkedRaw(bookMarkedRaw)
                .dailyMeal(dailyMeal)
                .build();
        Long dailyBookMarkedRawId = dailyBookMarkedRawRepository.save(dailyBookMarkedRaw).getId();


        // when
        rawService.deleteRawById(appleId);

        // then
        DailyRaw dailyRawDeletedRaw = dailyRawRepository.findById(dailyRawId).get();  // Raw를 제거해도 DailyRaw의 영양성분에는 영향을 미치지 않음.
        Assertions.assertEquals(30, dailyRawDeletedRaw.getKcal());
        Assertions.assertEquals(null, dailyRawDeletedRaw.getRaw());

        DailyBookMarkedRaw dailyBookMarkedRawDeletedRaw = dailyBookMarkedRawRepository.findById(dailyBookMarkedRawId).get();
        Assertions.assertEquals(null, dailyBookMarkedRawDeletedRaw.getBookMarkedRaw().getRaw());
        Assertions.assertEquals("존재하지 않는 자연식입니다.",dailyBookMarkedRawDeletedRaw.getBookMarkedRaw().getName());

        DailyMeal dailyMealDeletedRaw = dailyMealRepository.findById(dailyMealId).get();  // Raw를 제거해도 DailyMeal에는 영향을 미치지 않음
        Assertions.assertEquals(60, dailyMealDeletedRaw.getKcal());
        Assertions.assertEquals(20, dailyMealDeletedRaw.getNutrient().getProtein());
    }
}