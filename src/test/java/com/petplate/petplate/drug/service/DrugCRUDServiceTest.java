package com.petplate.petplate.drug.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static reactor.core.publisher.Mono.when;

import com.petplate.petplate.common.EmbeddedType.StandardNutrient;
import com.petplate.petplate.drug.domain.entity.Drug;
import com.petplate.petplate.drug.domain.entity.DrugDrugUsefulPart;
import com.petplate.petplate.drug.domain.entity.DrugNutrient;
import com.petplate.petplate.drug.domain.entity.DrugUsefulPart;
import com.petplate.petplate.drug.dto.request.DrugFindRequestDto;
import com.petplate.petplate.drug.dto.request.DrugSaveRequestDto;
import com.petplate.petplate.drug.dto.response.DrugResponseDto;
import com.petplate.petplate.drug.repository.DrugDrugUsefulPartRepository;
import com.petplate.petplate.drug.repository.DrugNutrientRepository;
import com.petplate.petplate.drug.repository.DrugRepository;
import com.petplate.petplate.drug.repository.DrugUsefulPartRepository;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.test.util.ReflectionTestUtils;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class DrugCRUDServiceTest {

    @InjectMocks
    DrugCRUDService drugCRUDService;

    @Mock
    DrugRepository drugRepository;

    @Mock
    DrugNutrientRepository drugNutrientRepository;

    @Mock
    DrugDrugUsefulPartRepository drugDrugUsefulPartRepository;

    @Mock
    DrugUsefulPartRepository drugUsefulPartRepository;

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

    private List<DrugDrugUsefulPart> getTestDrugDrugUsefulPartAndDrugUsefulPart(Drug drug){

        DrugUsefulPart drugUsefulPart1 = DrugUsefulPart.builder()
                .name("피부")
                .build();

        ReflectionTestUtils.setField(drugUsefulPart1,"id",1L);

        DrugUsefulPart drugUsefulPart2 = DrugUsefulPart.builder()
                .name("털")
                .build();

        ReflectionTestUtils.setField(drugUsefulPart2,"id",2L);

        DrugUsefulPart drugUsefulPart3 = DrugUsefulPart.builder()
                .name("관절")
                .build();

        ReflectionTestUtils.setField(drugUsefulPart3,"id",3L);

        DrugDrugUsefulPart drugDrugUsefulPart1 = DrugDrugUsefulPart.builder()
                .drugUsefulPart(drugUsefulPart1)
                .drug(drug)
                .build();

        ReflectionTestUtils.setField(drugDrugUsefulPart1,"id",1L);

        DrugDrugUsefulPart drugDrugUsefulPart2 = DrugDrugUsefulPart.builder()
                .drugUsefulPart(drugUsefulPart2)
                .drug(drug)
                .build();

        ReflectionTestUtils.setField(drugDrugUsefulPart2,"id",2L);

        DrugDrugUsefulPart drugDrugUsefulPart3 = DrugDrugUsefulPart.builder()
                .drugUsefulPart(drugUsefulPart3)
                .drug(drug)
                .build();

        ReflectionTestUtils.setField(drugDrugUsefulPart3,"id",3L);



        return List.of(drugDrugUsefulPart1,drugDrugUsefulPart2,drugDrugUsefulPart3);

    }

    @Test
    @DisplayName("Drug 저장 확인")
    public void 영양제_저장(){

        //given
        Drug drug = getTestDrug();
        List<DrugNutrient> drugNutrientList = getTestDrugNutrientList(drug);
        List<DrugDrugUsefulPart> drugDrugUsefulPartList = getTestDrugDrugUsefulPartAndDrugUsefulPart(drug);


        DrugSaveRequestDto drugSaveRequestDto = DrugSaveRequestDto.builder()
                .drugImgPath("img.path")
                .efficientNutrients(List.of("탄수화물","단백질"))
                .drugUsefulPartList(List.of(1L,2L,3L))
                .vendor("daum")
                .name("테스트drug")
                .englishName("testDrug")
                .url("www.naver.com")
                .build();

        given(drugRepository.save(any(Drug.class))).willReturn(drug);
        given(drugNutrientRepository.save(any(DrugNutrient.class))).willReturn(drugNutrientList.get(0));
        given(drugUsefulPartRepository.findById(eq(1L))).willReturn(Optional.ofNullable(drugDrugUsefulPartList.get(0).getDrugUsefulPart()));
        given(drugUsefulPartRepository.findById(eq(2L))).willReturn(Optional.ofNullable(drugDrugUsefulPartList.get(1).getDrugUsefulPart()));
        given(drugUsefulPartRepository.findById(eq(3L))).willReturn(Optional.ofNullable(drugDrugUsefulPartList.get(2).getDrugUsefulPart()));

        Mockito.when(drugDrugUsefulPartRepository.save(any(DrugDrugUsefulPart.class))).thenAnswer((Answer<DrugDrugUsefulPart>) invocation -> {
            DrugDrugUsefulPart drugDrugUsefulPart = invocation.getArgument(0);
            if (drugDrugUsefulPart.getDrug().getId().equals(1L)) {
                return drugDrugUsefulPartList.get(0);
            } else if (drugDrugUsefulPart.getDrug().getId().equals(2L)) {
                return drugDrugUsefulPartList.get(1);
            } else {
                return drugDrugUsefulPartList.get(2);
            }
        });


        System.out.println(drug.getId());

        //when
        Long resultDrugId = drugCRUDService.saveDrug(drugSaveRequestDto);

        //then
        assertThat(resultDrugId).isEqualTo(1L);

    }


    @Test
    @DisplayName("Drug 단일 조회")
    public void 영양제_단일_조회(){

        //given
        Drug drug = getTestDrug();
        List<DrugNutrient> drugNutrientList = getTestDrugNutrientList(drug);
        List<DrugDrugUsefulPart> drugDrugUsefulPartList = getTestDrugDrugUsefulPartAndDrugUsefulPart(drug);

        given(drugNutrientRepository.findByDrugIdWithFetchDrug(anyLong())).willReturn(drugNutrientList);
        given(drugRepository.findById(anyLong())).willReturn(Optional.of(drug));
        given(drugDrugUsefulPartRepository.findByDrugIdWithFetchDrugUsefulPart(anyLong())).willReturn(drugDrugUsefulPartList);

        //when
        DrugResponseDto drugResponseDto = drugCRUDService.showDrug(drug.getId());



        //then
        assertThat(drugResponseDto.getName()).isEqualTo("테스트drug");
        assertThat(drugResponseDto.getNutrientsName().get(0)).isEqualTo("단백질");
        assertThat(drugResponseDto.getDrugUsefulPartsName().get(0)).isEqualTo("피부");
        assertThat(drugResponseDto.getDrugUsefulPartsName().get(1)).isEqualTo("털");
        assertThat(drugResponseDto.getDrugUsefulPartsName().get(2)).isEqualTo("관절");

    }

    @Test
    @DisplayName("모든 영양소 리턴 ")
    public void ENUM_영양소_확인(){

        //given


        //when

        //then
        assertThat(drugCRUDService.showAllNutrientName().getNutrientList().size()).isEqualTo(8);


    }


    @Test
    @DisplayName("ID 기반 영양제 삭제-정상상황")
    public void ID기반_영양제_삭제(){

        //given
        Drug drug = getTestDrug();
        List<DrugNutrient> drugNutrientList = getTestDrugNutrientList(drug);

        given(drugRepository.existsById(anyLong())).willReturn(true);


        //when
        drugCRUDService.deleteDrug(drug.getId());



        //then
        verify(drugRepository,times(1)).deleteById(1L);
        verify(drugNutrientRepository,times(1)).deleteByDrugId(1L);
        verify(drugDrugUsefulPartRepository,times(1)).deleteByDrugId(1L);


    }



    @Test
    @DisplayName("영양제 리스트 전체 조회")
    public void 영양제_전체_영양제_조회(){

        //given
        List<Drug> drugList=List.of(getTestDrug(),getTestDrug());
        given(drugRepository.findAll()).willReturn(drugList);

        //when
        List<DrugResponseDto> drugResponseDtoList = drugCRUDService.showAllDrug();


        //then
        assertThat(drugResponseDtoList.size()).isEqualTo(2);
        assertThat(drugResponseDtoList.get(0).getId()).isEqualTo(1L);
    }







}