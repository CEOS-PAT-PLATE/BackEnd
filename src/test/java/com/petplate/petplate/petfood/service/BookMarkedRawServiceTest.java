package com.petplate.petplate.petfood.service;

import com.petplate.petplate.common.EmbeddedType.Nutrient;
import com.petplate.petplate.common.EmbeddedType.Vitamin;
import com.petplate.petplate.common.response.error.exception.BadRequestException;
import com.petplate.petplate.common.response.error.exception.NotFoundException;
import com.petplate.petplate.pet.domain.Activity;
import com.petplate.petplate.pet.domain.Neutering;
import com.petplate.petplate.pet.domain.entity.Pet;
import com.petplate.petplate.pet.dto.request.CreatePetRequestDto;
import com.petplate.petplate.pet.repository.PetRepository;
import com.petplate.petplate.petdailymeal.domain.entity.DailyBookMarkedRaw;
import com.petplate.petplate.petdailymeal.domain.entity.DailyMeal;
import com.petplate.petplate.petdailymeal.repository.DailyBookMarkedRawRepository;
import com.petplate.petplate.petdailymeal.repository.DailyMealRepository;
import com.petplate.petplate.petfood.domain.entity.BookMarkedRaw;
import com.petplate.petplate.petfood.domain.entity.Raw;
import com.petplate.petplate.petfood.dto.request.CreateBookMarkedRawRequestDto;
import com.petplate.petplate.petfood.dto.response.ReadBookMarkedRawResponseDto;
import com.petplate.petplate.petfood.repository.BookMarkedRawRepository;
import com.petplate.petplate.petfood.repository.RawRepository;
import com.petplate.petplate.user.domain.Role;
import com.petplate.petplate.user.domain.SocialType;
import com.petplate.petplate.user.domain.entity.User;
import com.petplate.petplate.user.domain.entity.UserMemberShip;
import com.petplate.petplate.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BookMarkedRawServiceTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PetRepository petRepository;
    @Autowired
    private RawRepository rawRepository;
    @Autowired
    private BookMarkedRawService bookMarkedRawService;
    @Autowired
    private BookMarkedRawRepository bookMarkedRawRepository;
    @Autowired
    private DailyMealRepository dailyMealRepository;

    private String username;
    private Long petId;
    private Long appleId;
    private Long bananaId;
    @Autowired
    private DailyBookMarkedRawRepository dailyBookMarkedRawRepository;

    @BeforeEach
    public void each() {
        User user =
                User.builder().name("user").role(Role.ADMIN)
                        .username("aaaa").password("aaaa")
                        .activated(true).isReceiveAd(true)
                        .phoneNumber("010-1111-1111")
                        .socialType(SocialType.NAVER)
                        .build();

        username = userRepository.save(user).getUsername();

        Pet pet = Pet.builder()
                .owner(user)
                .name("test pet")
                .age(11)
                .weight(10)
                .activity(Activity.ACTIVE)
                .neutering(Neutering.NEUTERED)
                .build();
        petId = petRepository.save(pet).getId();

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
        appleId = rawRepository.save(apple).getId();

        Raw banana = Raw.builder()
                .name("바나나")
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
        bananaId = rawRepository.save(banana).getId();
    }

    @Test
    @DisplayName("즐겨찾기 자연식 생성")
    void createBookMarkedRaw() {
        //given
        CreateBookMarkedRawRequestDto createBookMarkedRawRequestDto = new CreateBookMarkedRawRequestDto(appleId, 200);
        Long id = bookMarkedRawService.createBookMarkedRaw(username, createBookMarkedRawRequestDto);

        BookMarkedRaw bookMarkedRaw = bookMarkedRawRepository.findById(id).get();

        // then
        Assertions.assertEquals(bookMarkedRaw.getRaw().getKcal() * (bookMarkedRaw.getServing() / bookMarkedRaw.getRaw().getStandardAmount()), bookMarkedRaw.getKcal());
        Assertions.assertEquals(bookMarkedRaw.getRaw().getNutrient().getVitamin().getVitaminD() * (bookMarkedRaw.getServing() / bookMarkedRaw.getRaw().getStandardAmount()), bookMarkedRaw.getNutrient().getVitamin().getVitaminD());
        Assertions.assertEquals(appleId, bookMarkedRaw.getRaw().getId());
        Assertions.assertEquals(200, bookMarkedRaw.getServing());

        Assertions.assertThrows(NotFoundException.class, () -> bookMarkedRawService.createBookMarkedRaw("asklfjdvxchio", createBookMarkedRawRequestDto));
        Assertions.assertThrows(NotFoundException.class, () -> bookMarkedRawService.createBookMarkedRaw(username, new CreateBookMarkedRawRequestDto(-12345L, 200)));
        Assertions.assertThrows(BadRequestException.class, () -> bookMarkedRawService.createBookMarkedRaw(username, createBookMarkedRawRequestDto));
    }

    @Test
    @DisplayName("즐겨찾기 자연식 조회")
    void getBookMarkedRaws() {
        // given
        User user = userRepository.findByUsername(username).get();
        BookMarkedRaw apple = BookMarkedRaw.builder()
                .raw(rawRepository.findById(appleId).get())
                .user(user)
                .serving(500)
                .build();
        bookMarkedRawRepository.save(apple);

        BookMarkedRaw banana = BookMarkedRaw.builder()
                .raw(rawRepository.findById(bananaId).get())
                .user(user)
                .serving(200)
                .build();
        bookMarkedRawRepository.save(banana);

        // when

        // then
        Assertions.assertEquals(2, bookMarkedRawService.getBookMarkedRaws(username).size());
    }

    @Test
    @DisplayName("id로 즐겨찾기 자연식 조회")
    void getBookMarkedRaw() {

        // given
        User user = userRepository.findByUsername(username).get();
        BookMarkedRaw apple = BookMarkedRaw.builder()
                .raw(rawRepository.findById(appleId).get())
                .user(user)
                .serving(500)
                .build();

        // when
        Long id = bookMarkedRawRepository.save(apple).getId();

        // then
        ReadBookMarkedRawResponseDto bookMarkedRaw = bookMarkedRawService.getBookMarkedRaw(username, id);
        Assertions.assertEquals(id, bookMarkedRaw.getId());
        Assertions.assertEquals(150, bookMarkedRaw.getKcal());
    }

    @Test
    @DisplayName("즐겨찾기 자연식 제거")
    void deleteBookMarkedRaw() {
        // given
        User user = userRepository.findByUsername(username).get();
        Pet pet = petRepository.findById(petId).get();

        BookMarkedRaw apple = BookMarkedRaw.builder()
                .raw(rawRepository.findById(appleId).get())
                .user(user)
                .serving(500)
                .build();

        Long id = bookMarkedRawRepository.save(apple).getId();

        DailyMeal dailyMeal = DailyMeal.builder()
                .pet(pet)
                .kcal(apple.getKcal())
                .nutrient(
                        Nutrient.builder()
                                .carbonHydrate(apple.getNutrient().getCarbonHydrate())
                                .protein(apple.getNutrient().getProtein())
                                .fat(apple.getNutrient().getFat())
                                .calcium(apple.getNutrient().getCalcium())
                                .phosphorus(apple.getNutrient().getPhosphorus())
                                .vitamin(Vitamin.builder().vitaminA(apple.getNutrient().getVitamin().getVitaminA()).vitaminD(apple.getNutrient().getVitamin().getVitaminD()).vitaminE(apple.getNutrient().getVitamin().getVitaminE()).build())
                                .build())
                .build();
        dailyMealRepository.save(dailyMeal).getId();

        DailyBookMarkedRaw dailyBookMarkedRaw = DailyBookMarkedRaw.builder()
                .bookMarkedRaw(apple)
                .dailyMeal(dailyMeal)
                .build();
        dailyBookMarkedRawRepository.save(dailyBookMarkedRaw);

        // when
        bookMarkedRawService.deleteBookMarkedRaw(username, id);

        // then
        Assertions.assertEquals(0, bookMarkedRawRepository.findByUserId(user.getId()).size());
        Assertions.assertNull(dailyBookMarkedRaw.getBookMarkedRaw());
        System.out.println("dailyMeal = " + dailyMeal.getKcal());
    }
}