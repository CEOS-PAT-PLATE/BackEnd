package com.petplate.petplate.drug.repository;

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
class DrugNutrientRepositoryTest {

    @Autowired
    private DrugNutrientRepository drugNutrientRepository;

    @Autowired
    private DrugRepository drugRepository;





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
    @DisplayName("DrugNutrient 저장 시도 - 저장")
    public void DN_저장(){

        //given
        Drug tmp = maketmpDrug();

        //when
        DrugNutrient dn = drugNutrientRepository.save(DrugNutrient.builder().drug(tmp).standardNutrient(StandardNutrient.CARBON_HYDRATE).build());


        //then
        assertThat(dn).isNotNull();
        assertThat(dn.getDrug().getName()).isEqualTo("영양제");
        assertThat(dn.getStandardNutrient()).isEqualTo(StandardNutrient.CARBON_HYDRATE);
    }

    @Test
    @DisplayName("Drug 로 DrugNutrient 리스트 조회")
    public void DN_영양제로_리스트_조회(){

        //given
        Drug tmp = maketmpDrug();

        DrugNutrient dn1 = drugNutrientRepository.save(DrugNutrient.builder().drug(tmp).standardNutrient(StandardNutrient.CARBON_HYDRATE).build());

        DrugNutrient dn2 = drugNutrientRepository.save(DrugNutrient.builder().drug(tmp).standardNutrient(StandardNutrient.VITAMIN_A).build());

        DrugNutrient dn3 = drugNutrientRepository.save(DrugNutrient.builder().drug(tmp).standardNutrient(StandardNutrient.VITAMIN_D).build());

        drugNutrientRepository.save(dn1);
        drugNutrientRepository.save(dn2);
        drugNutrientRepository.save(dn3);

        //when
        List<DrugNutrient> byDrug = drugNutrientRepository.findByDrug(tmp);

        //then
        assertThat(byDrug.size()).isEqualTo(3);
        assertThat(byDrug.get(0).getStandardNutrient()).isEqualTo(StandardNutrient.CARBON_HYDRATE);


    }

    @Test
    @DisplayName("Drug ID 로 DrugNutrient 리스트 조회 - 페치조인")
    public void DN_영양제아이디_DN_리스트(){

        //given
        Drug tmp = maketmpDrug();

        DrugNutrient dn1 = drugNutrientRepository.save(DrugNutrient.builder().drug(tmp).standardNutrient(StandardNutrient.CARBON_HYDRATE).build());
        DrugNutrient dn2 = drugNutrientRepository.save(DrugNutrient.builder().drug(tmp).standardNutrient(StandardNutrient.VITAMIN_A).build());
        DrugNutrient dn3 = drugNutrientRepository.save(DrugNutrient.builder().drug(tmp).standardNutrient(StandardNutrient.VITAMIN_D).build());

        drugNutrientRepository.save(dn1);
        drugNutrientRepository.save(dn2);
        drugNutrientRepository.save(dn3);

        //when
        List<DrugNutrient> byDrug = drugNutrientRepository.findByDrugIdWithFetchDrug(tmp.getId());

        //then
        assertThat(byDrug.size()).isEqualTo(3);
        assertThat(byDrug.get(0).getStandardNutrient()).isEqualTo(StandardNutrient.CARBON_HYDRATE);


    }

    @Test
    @DisplayName("단일 영양소를 포함하고 있는 영양제")
    public void DN_단일영양소로_찾는_영양제(){

        //given


        //when
        List<DrugNutrient> fatList = drugNutrientRepository.findByStandardNutrientWithFetchDrug(StandardNutrient.FAT);
        List<DrugNutrient> proteinList = drugNutrientRepository.findByStandardNutrientWithFetchDrug(StandardNutrient.PROTEIN);
        List<DrugNutrient> vitaminEList = drugNutrientRepository.findByStandardNutrientWithFetchDrug(StandardNutrient.VITAMIN_E);


        //then
        assertThat(fatList.size()).isEqualTo(2);
        assertThat(proteinList.size()).isEqualTo(3);
        assertThat(vitaminEList.size()).isEqualTo(0);

    }

    @Test
    @DisplayName("단일 영양제 iD 기반 DrugNutrient 삭제")
    public void DN_단일_영양제_ID_기반_DRUG_NUTRIENT_삭제(){

        //given
        Drug drug = maketmpDrug();

        DrugNutrient drugNutrient = DrugNutrient.builder()
                .standardNutrient(StandardNutrient.VITAMIN_E)
                .drug(drug)
                .build();

        drugNutrientRepository.save(drugNutrient);


        //when
        drugNutrientRepository.deleteByDrugId(drug.getId());



        //then
        assertThat(drugNutrientRepository.findByDrug(drug)).isEmpty();


    }


    private Drug maketmpDrug(){

        Drug tmp =   drugRepository.save(Drug.builder()
                .drugImgPath("fwf")
                .vendor("aaa")
                .englishName("bbb")
                .url("www")
                .name("영양제").build());

        return tmp;
    }


}