package com.petplate.petplate.drug.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.petplate.petplate.common.EmbeddedType.StandardNutrient;
import com.petplate.petplate.drug.domain.entity.Drug;
import com.petplate.petplate.drug.domain.entity.DrugNutrient;
import com.petplate.petplate.drug.dto.request.DrugFindRequestDto;
import com.petplate.petplate.drug.dto.response.DrugResponseDto;
import com.petplate.petplate.drug.repository.DrugNutrientRepository;
import com.petplate.petplate.drug.repository.DrugRepository;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class DrugRecommendServiceTest {

    @InjectMocks
    DrugRecommendService drugRecommendService;

    @Mock
    DrugRepository drugRepository;

    @Mock
    DrugNutrientRepository drugNutrientRepository;


    private Drug getTestDrug(){

        Drug drug = Drug.builder()
                .drugImgPath("img.path")
                .name("테스트drug")
                .englishName("testDrug")
                .vendor("naver")
                .url("www.naver.com")
                .build();

        ReflectionTestUtils.setField(drug,"id",1L);

        return drug;
    }

    private List<DrugNutrient> getTestDrugNutrientList(Drug drug){

        DrugNutrient drugNutrient1 =DrugNutrient.builder()
                .drug(drug)
                .standardNutrient(StandardNutrient.PROTEIN)
                .build();

        ReflectionTestUtils.setField(drugNutrient1,"id",2L);

        DrugNutrient drugNutrient2 =DrugNutrient.builder()
                .drug(drug)
                .standardNutrient(StandardNutrient.CARBON_HYDRATE)
                .build();

        ReflectionTestUtils.setField(drugNutrient2,"id",3L);

        DrugNutrient drugNutrient3 =DrugNutrient.builder()
                .drug(drug)
                .standardNutrient(StandardNutrient.PHOSPHORUS)
                .build();

        ReflectionTestUtils.setField(drugNutrient3,"id",4L);

        return List.of(drugNutrient1,drugNutrient2,drugNutrient3);

    }


    @Test
    @DisplayName("특정 영양소 하나를 포함하는 영양제 조회")
    public void 영양소_하나로_영양제_조회(){
        //given
        Drug drug = getTestDrug();
        List<DrugNutrient> drugNutrientList = getTestDrugNutrientList(drug);
        given(drugNutrientRepository.findByStandardNutrientWithFetchDrug(any(StandardNutrient.class))).willReturn(List.of(drugNutrientList.get(1)));
        given(drugNutrientRepository.findByDrug(any(Drug.class))).willReturn(drugNutrientList);


        //when
        List<DrugResponseDto> drugResponseDtoList = drugRecommendService.findDrugByNutrientName("탄수화물");


        //then
        assertThat(drugResponseDtoList.size()).isEqualTo(1);
        assertThat(drugResponseDtoList.get(0).getNutrientsName().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("특정 영양소를 가장많이 포함하는 영양제 순서대로 조회")
    public void 영양소_여러개로_영양제_조회(){
        //given
        Drug drug = getTestDrug();

        List<DrugNutrient> drugNutrientList = getTestDrugNutrientList(drug);

        given(drugRepository.findUserProperDrugList(eq(List.of(StandardNutrient.CARBON_HYDRATE,StandardNutrient.PROTEIN)))
        ).willReturn(List.of(drug));


        //when
        List<DrugResponseDto> drugResponseDtoList = drugRecommendService.findDrugByVariousNutrientName(
                DrugFindRequestDto.builder()
                        .nutrients(List.of("탄수화물","단백질"))
                        .build());




        //then
        assertThat(drugResponseDtoList.size()).isEqualTo(1);
        assertThat(drugResponseDtoList.get(0).getNutrientsName().size()).isEqualTo(3);
    }

}