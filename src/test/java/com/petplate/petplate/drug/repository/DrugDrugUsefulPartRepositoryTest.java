package com.petplate.petplate.drug.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.petplate.petplate.common.EmbeddedType.StandardNutrient;
import com.petplate.petplate.drug.domain.entity.Drug;
import com.petplate.petplate.drug.domain.entity.DrugDrugUsefulPart;
import com.petplate.petplate.drug.domain.entity.DrugNutrient;
import com.petplate.petplate.drug.domain.entity.DrugUsefulPart;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.ANY)
class DrugDrugUsefulPartRepositoryTest {

    @Autowired
    private DrugUsefulPartRepository drugUsefulPartRepository;

    @Autowired
    private DrugDrugUsefulPartRepository drugDrugUsefulPartRepository;

    @Autowired
    private DrugRepository drugRepository;





    @BeforeEach
    public void initDrugNutrientAndDrug(){

        //피부와 털에 좋음
        Drug drug1 = Drug.builder()
                .drugImgPath("img.path1.com")
                .name("영양제A")
                .vendor("네이버")
                .englishName("DrugA")
                .url("www.naver.com")
                .build();

        //털과 관절에 좋음
        Drug drug2 = Drug.builder()
                .drugImgPath("img.path2.com")
                .name("영양제B")
                .vendor("다음")
                .englishName("DrugB")
                .url("www.daum.net")
                .build();


        //피부와 관절에 좋음
        Drug drug3 = Drug.builder()
                .drugImgPath("img.path3.com")
                .name("영양제C")
                .vendor("구글")
                .englishName("DrugC")
                .url("www.google.com")
                .build();

        DrugUsefulPart drugUsefulPart1 = DrugUsefulPart.builder()
                .name("피부")
                .build();

        DrugUsefulPart drugUsefulPart2 = DrugUsefulPart.builder()
                .name("털")
                .build();

        DrugUsefulPart drugUsefulPart3 = DrugUsefulPart.builder()
                .name("관절")
                .build();

        DrugDrugUsefulPart drugDrugUsefulPart1 = DrugDrugUsefulPart.builder()
                .drugUsefulPart(drugUsefulPart1)
                        .drug(drug1)
                                .build();

        DrugDrugUsefulPart drugDrugUsefulPart2 = DrugDrugUsefulPart.builder()
                .drugUsefulPart(drugUsefulPart2)
                .drug(drug1)
                .build();

        DrugDrugUsefulPart drugDrugUsefulPart3 = DrugDrugUsefulPart.builder()
                .drugUsefulPart(drugUsefulPart2)
                .drug(drug2)
                .build();

        DrugDrugUsefulPart drugDrugUsefulPart4 = DrugDrugUsefulPart.builder()
                .drugUsefulPart(drugUsefulPart3)
                .drug(drug2)
                .build();

        DrugDrugUsefulPart drugDrugUsefulPart5 = DrugDrugUsefulPart.builder()
                .drugUsefulPart(drugUsefulPart1)
                .drug(drug3)
                .build();

        DrugDrugUsefulPart drugDrugUsefulPart6 = DrugDrugUsefulPart.builder()
                .drugUsefulPart(drugUsefulPart3)
                .drug(drug3)
                .build();

        // drug 1 에는 영양이 FAT,PROTEIN, CARBON_HYDRATE

        drugRepository.save(drug1);
        drugRepository.save(drug2);
        drugRepository.save(drug3);
        drugUsefulPartRepository.save(drugUsefulPart1);
        drugUsefulPartRepository.save(drugUsefulPart2);
        drugUsefulPartRepository.save(drugUsefulPart3);
        drugDrugUsefulPartRepository.save(drugDrugUsefulPart1);
        drugDrugUsefulPartRepository.save(drugDrugUsefulPart2);
        drugDrugUsefulPartRepository.save(drugDrugUsefulPart3);
        drugDrugUsefulPartRepository.save(drugDrugUsefulPart4);
        drugDrugUsefulPartRepository.save(drugDrugUsefulPart5);
        drugDrugUsefulPartRepository.save(drugDrugUsefulPart6);

    }

    @Test
    @DisplayName("drug id 기반 DurgDrugUsefulPart 조회")
    public void 약_ID_기반_DrugDrugUsefulPart_조회(){

        //given
        Drug drug = drugRepository.findAll().get(0);

        //when
        List<DrugDrugUsefulPart> drugDrugUsefulPartList = drugDrugUsefulPartRepository.findByDrugId(drug.getId());

        //then
        assertThat(drugDrugUsefulPartList.get(0).getDrugUsefulPart().getName()).isEqualTo("피부");
        assertThat(drugDrugUsefulPartList.get(1).getDrugUsefulPart().getName()).isEqualTo("털");


    }

    @Test
    @DisplayName("drug id 기반 DurgDrugUsefulPart 조회-페치조인")
    public void 약_ID_기반_DrugDrugUsefulPart_조회_페치조인(){

        //given
        Drug drug = drugRepository.findAll().get(0);

        //when
        List<DrugDrugUsefulPart> drugDrugUsefulPartList = drugDrugUsefulPartRepository.findByDrugIdWithFetchDrugUsefulPart(drug.getId());

        //then
        assertThat(drugDrugUsefulPartList.get(0).getDrugUsefulPart().getName()).isEqualTo("피부");
        assertThat(drugDrugUsefulPartList.get(1).getDrugUsefulPart().getName()).isEqualTo("털");


    }




}