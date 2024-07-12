package com.petplate.petplate.pet.domain.entity;

import com.petplate.petplate.pet.domain.Activity;
import com.petplate.petplate.pet.domain.Neutering;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PetTest {

    @Test
    void rer() {
        double rer = 70 * Math.pow(4.32, 0.75);

        System.out.println("rer = " + rer);  //210
    }

    @Test
    void getProperKcal() {
        Pet pet = Pet.builder()
                .name("pet")
                .age(5)
                .activity(Activity.SOMEWHAT_ACTIVE)
                .neutering(Neutering.NEUTERED)
                .owner(null)
                .weight(29.55)
                .build();

        double properKcal = pet.getProperKcal();

        Assertions.assertEquals(1703, (int) properKcal);
    }

    @Test
    void testGetProperKcal() {
    }
}