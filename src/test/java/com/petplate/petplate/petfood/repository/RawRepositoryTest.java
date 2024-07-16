package com.petplate.petplate.petfood.repository;

import com.petplate.petplate.petfood.domain.entity.Raw;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RawRepositoryTest {
    @Autowired
    private RawRepository rawRepository;
    
    @Test
    void findByKeyword() {
        List<Raw> 돼 = rawRepository.findByKeyword("돼");

        for (Raw raw : 돼) {
            System.out.println("raw.getName() = " + raw.getName());
        }

    }
}