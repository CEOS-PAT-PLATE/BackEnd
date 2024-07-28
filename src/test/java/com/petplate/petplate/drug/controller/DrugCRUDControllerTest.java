package com.petplate.petplate.drug.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.petplate.petplate.common.response.error.RestExceptionHandler;
import com.petplate.petplate.drug.domain.entity.Drug;
import com.petplate.petplate.drug.dto.request.DrugSaveRequestDto;
import com.petplate.petplate.drug.dto.response.DrugResponseDto;
import com.petplate.petplate.drug.dto.response.ShowNutrientListResponseDto;
import com.petplate.petplate.drug.service.DrugCRUDService;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@WebMvcTest(DrugCRUDControllerTest.class)
@MockBean(JpaMetamodelMappingContext.class)
class DrugCRUDControllerTest {


    @InjectMocks
    private DrugCRUDController drugCRUDController;

    @Mock
    private DrugCRUDService drugCRUDService;


    private MockMvc mockMvc;

    private Gson gson;

    private static final String DRUG= "/api/v1/drugs";

    @BeforeEach
    public void init() {
        GsonBuilder gsonBuilder = new GsonBuilder();

        gson = gsonBuilder.setPrettyPrinting().create();

        mockMvc = MockMvcBuilders.standaloneSetup(drugCRUDController)
                .setControllerAdvice(new RestExceptionHandler())
                .setCustomArgumentResolvers(new AuthenticationPrincipalArgumentResolver())
                .build();


    }

    private DrugSaveRequestDto drugSaveRequestDto(final String imgPath,final String name,final String englishName,final String url,
            final List<String> efficientNutrients,final String vendor,final List<Long> drugUsefulPartList){

        return DrugSaveRequestDto.builder()
                .drugImgPath(imgPath)
                .englishName(englishName)
                .name(name)
                .efficientNutrients(efficientNutrients)
                .drugUsefulPartList(drugUsefulPartList)
                .vendor(vendor)
                .url(url)
                .build();

    }


    @Test
    @DisplayName("정상적인 영양제 저장 상황")
    public void 영양제데이터_정상_저장() throws Exception {
        //given

        when(drugCRUDService.saveDrug(any(DrugSaveRequestDto.class))).thenReturn(1L);

        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(DRUG)
                        .content(gson.toJson(drugSaveRequestDto("www.img","약국","druglol",
                                "www.naver.com",List.of("탄수화물","단백질"),"네이버",List.of(3L))))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then

        resultActions.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.data").value(1L));


    }

    @Test
    @DisplayName("정상적인 영양제 단일조회 상황")
    public void 영양제데이터_정상_단일_조회() throws Exception {
        //given

        Drug drug = Drug.builder()
                        .drugImgPath("www.img")
                                .name("제조약")
                                        .englishName("englishName")
                                                .vendor("naver")
                                                        .url("www.naver.com")
                                                                .build();

        List<String> nutrientsName = List.of("탄수화물","단백질");
        List<String> drugUsefulPartsName =List.of("암예방");
        given(drugCRUDService.showDrug(anyLong())).willReturn(DrugResponseDto.of(drug,nutrientsName,drugUsefulPartsName));

        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(DRUG+"/{id}",1)
                        .contentType(MediaType.APPLICATION_JSON)
        );


        //then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.data.name").value("제조약"))
                .andExpect(jsonPath("$.data.nutrientsName[0]").value("탄수화물"))
                .andExpect(jsonPath("$.data.drugUsefulPartsName[0]").value("암예방"));



    }

    @Test
    @DisplayName("전체 영양소 조회")
    public void 전체_영양소_조회() throws Exception {

        //given
        given(drugCRUDService.showAllNutrientName()).willReturn(ShowNutrientListResponseDto.from(List.of("탄수화물")));


        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(DRUG+"/standard-nutrients")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.data.nutrientList[0]").value("탄수화물"));


    }


    @Test
    @DisplayName("단일 영양제 삭제 ID 기반-정상")
    public void 단일_영양제_삭제_ID_기반() throws Exception {

        //given
        doNothing().when(drugCRUDService).deleteDrug(anyLong());


        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.delete(DRUG+"/{id}",1)
                        .contentType(MediaType.APPLICATION_JSON)
        );


        //then
        resultActions.andExpect(status().isOk());

    }


    @Test
    @DisplayName("전체 영양제 조회")
    public void 전체_영양제_조회() throws Exception {

        //given
        Drug drug = Drug.builder()
                .drugImgPath("www.img")
                .name("제조약")
                .englishName("englishName")
                .vendor("naver")
                .url("www.naver.com")
                .build();

        List<String> nutrientsName = List.of("탄수화물","단백질");
        List<String> drugUsefulPartsName =List.of("암예방");
        given(drugCRUDService.showAllDrug()).willReturn(List.of(DrugResponseDto.of(drug,nutrientsName,drugUsefulPartsName)));


        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(DRUG)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.data[0].name").value("제조약"))
                .andExpect(jsonPath("$.data[0].nutrientsName[0]").value("탄수화물"))
                .andExpect(jsonPath("$.data[0].drugUsefulPartsName[0]").value("암예방"));


    }













}