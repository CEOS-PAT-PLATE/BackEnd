package com.petplate.petplate.drug.repository;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.petplate.petplate.common.EmbeddedType.StandardNutrient;
import com.petplate.petplate.drug.domain.entity.Drug;
import com.petplate.petplate.drug.domain.entity.DrugNutrient;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.ANY)
class DrugRepositoryTest {

    @Autowired
    private DrugRepository drugRepository;

    @Autowired
    private DrugNutrientRepository drugNutrientRepository;

    @BeforeEach
    public void initDrugNutrientAndDrug(){

        Drug drug1 = Drug.builder()
                .drugImgPath("img.path1.com")
                .name("영양제A")
                .vendor("네이버")
                .englishName("DrugA")
                .url("www.naver.com")
                .build();

        Drug drug2 = Drug.builder()
                .drugImgPath("img.path2.com")
                .name("영양제B")
                .vendor("다음")
                .englishName("DrugB")
                .url("www.daum.net")
                .build();


        Drug drug3 = Drug.builder()
                .drugImgPath("img.path3.com")
                .name("영양제C")
                .vendor("구글")
                .englishName("DrugC")
                .url("www.google.com")
                .build();

        DrugNutrient drugNutrient1 = DrugNutrient.builder()
                .drug(drug1)
                .standardNutrient(StandardNutrient.CARBON_HYDRATE)
                .build();

        DrugNutrient drugNutrient2 = DrugNutrient.builder()
                .drug(drug1)
                .standardNutrient(StandardNutrient.PROTEIN)
                .build();

        DrugNutrient drugNutrient3 = DrugNutrient.builder()
                .drug(drug1)
                .standardNutrient(StandardNutrient.FAT)
                .build();
        // drug 1 에는 영양이 FAT,PROTEIN, CARBON_HYDRATE

        drugRepository.save(drug1);
        drugNutrientRepository.save(drugNutrient1);
        drugNutrientRepository.save(drugNutrient2);
        drugNutrientRepository.save(drugNutrient3);







        DrugNutrient drugNutrient4 = DrugNutrient.builder()
                .drug(drug2)
                .standardNutrient(StandardNutrient.PROTEIN)
                .build();

        DrugNutrient drugNutrient5 = DrugNutrient.builder()
                .drug(drug2)
                .standardNutrient(StandardNutrient.FAT)
                .build();

        DrugNutrient drugNutrient6 = DrugNutrient.builder()
                .drug(drug2)
                .standardNutrient(StandardNutrient.PHOSPHORUS)
                .build();

        // drug 2 에는 영양이 FAT,PROTEIN, PHOSPHORUS

        drugRepository.save(drug2);
        drugNutrientRepository.save(drugNutrient4);
        drugNutrientRepository.save(drugNutrient5);
        drugNutrientRepository.save(drugNutrient6);





        DrugNutrient drugNutrient7 = DrugNutrient.builder()
                .drug(drug3)
                .standardNutrient(StandardNutrient.VITAMIN_A)
                .build();

        DrugNutrient drugNutrient8 = DrugNutrient.builder()
                .drug(drug3)
                .standardNutrient(StandardNutrient.PROTEIN)
                .build();

        // drug 3 에는 영양이 VITAMIN_ A, PROTEIN
        drugRepository.save(drug3);
        drugNutrientRepository.save(drugNutrient7);
        drugNutrientRepository.save(drugNutrient8);


    }


    @Test
    @DisplayName("영양소 리스트 중 가장 많이 부합하는 영양제를 순서대로 찾기")
    public void 영양소_리스트_부합_영양제(){

        List<Drug> userProperDrugList1 = drugRepository.findUserProperDrugList(
                List.of(StandardNutrient.VITAMIN_A, StandardNutrient.PROTEIN));

        List<Drug> userProperDrugList2 = drugRepository.findUserProperDrugList(List.of(StandardNutrient.FAT,
                StandardNutrient.CARBON_HYDRATE));

        List<Drug> userProperDrugList3 = drugRepository.findUserProperDrugList(List.of(StandardNutrient.FAT,StandardNutrient.PROTEIN,
                StandardNutrient.PHOSPHORUS));

        assertThat(userProperDrugList1.size()).isEqualTo(3);
        assertThat(userProperDrugList1.get(0).getName()).isEqualTo("영양제C");

        assertThat(userProperDrugList2.size()).isEqualTo(2);
        assertThat(userProperDrugList2.get(0).getName()).isEqualTo("영양제A");
        assertThat(userProperDrugList2.get(1).getName()).isEqualTo("영양제B");

        assertThat(userProperDrugList3.size()).isEqualTo(3);
        assertThat(userProperDrugList3.get(0).getName()).isEqualTo("영양제B");
        assertThat(userProperDrugList3.get(1).getName()).isEqualTo("영양제A");
        assertThat(userProperDrugList3.get(2).getName()).isEqualTo("영양제C");


    }


    @Test
    @DisplayName("전체 영양제 리스트 확인")
    public void 전체_영양제_리스트_확인(){

        List<Drug> drugList = drugRepository.findAll();

        assertThat(drugList.size()).isEqualTo(3);
    }


}