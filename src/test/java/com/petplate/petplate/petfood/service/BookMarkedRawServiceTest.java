package com.petplate.petplate.petfood.service;

import com.petplate.petplate.common.EmbeddedType.Nutrient;
import com.petplate.petplate.common.EmbeddedType.Vitamin;
import com.petplate.petplate.common.response.error.exception.NotFoundException;
import com.petplate.petplate.pet.domain.Activity;
import com.petplate.petplate.pet.domain.Neutering;
import com.petplate.petplate.pet.domain.entity.Pet;
import com.petplate.petplate.pet.dto.request.CreatePetRequestDto;
import com.petplate.petplate.pet.repository.PetRepository;
import com.petplate.petplate.petfood.domain.entity.BookMarkedRaw;
import com.petplate.petplate.petfood.domain.entity.Raw;
import com.petplate.petplate.petfood.dto.request.CreateBookMarkedRawRequestDto;
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

    private String username;
    private Long petId;
    private Long appleId;

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
    }

    @Test
    void getBookMarkedRaws() {
    }

    @Test
    void getBookMarkedRaw() {
    }

    @Test
    void deleteBookMarkedRaw() {
    }
}