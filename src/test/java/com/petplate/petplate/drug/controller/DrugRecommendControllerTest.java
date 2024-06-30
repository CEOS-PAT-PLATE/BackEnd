package com.petplate.petplate.drug.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.petplate.petplate.common.response.error.RestExceptionHandler;
import com.petplate.petplate.drug.domain.entity.Drug;
import com.petplate.petplate.drug.dto.request.DrugFindRequestDto;
import com.petplate.petplate.drug.dto.request.DrugSaveRequestDto;
import com.petplate.petplate.drug.dto.response.DrugResponseDto;
import com.petplate.petplate.drug.dto.response.RecommendDrugResponseDto;
import com.petplate.petplate.drug.service.DrugCRUDService;
import com.petplate.petplate.drug.service.DrugRecommendService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.BDDMockito.given;

@WebMvcTest(DrugRecommendControllerTest.class)
@MockBean(JpaMetamodelMappingContext.class)
class DrugRecommendControllerTest {

    @InjectMocks
    private DrugRecommendController drugRecommendController;

    @Mock
    private DrugRecommendService drugRecommendService;


    private MockMvc mockMvc;

    private Gson gson;

    private static final String DRUG_RECOMMEND= "/drug/recommend";

    @BeforeEach
    public void init() {
        GsonBuilder gsonBuilder = new GsonBuilder();

        gson = gsonBuilder.setPrettyPrinting().create();

        mockMvc = MockMvcBuilders.standaloneSetup(drugRecommendController)
                .setControllerAdvice(new RestExceptionHandler())
                .setCustomArgumentResolvers(new AuthenticationPrincipalArgumentResolver())
                .build();


    }

    @Test
    @DisplayName("한 영양소로 추천")
    public void 영양제_추천_단일_영양소() throws Exception {
        //given

        given(drugRecommendService.findDrugByNutrientName(any(String.class))).willReturn(List.of(
                RecommendDrugResponseDto.from(Drug.builder()
                        .drugImgPath("img")
                        .url("www.naver")
                        .vendor("vendor")
                        .englishName("english")
                        .name("한글").build()
                )));

        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(DRUG_RECOMMEND+"/proper")
                        .param("nutrient","탄수화물")
                        .contentType(MediaType.APPLICATION_JSON)
        );


        //then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.data[0].name").value("한글"));


    }

    @Test
    @DisplayName("다중 영양소로 추천")
    public void 영양제_추천_다중_영양소() throws Exception {
        //given

        given(drugRecommendService.findDrugByVariousNutrientName(any(DrugFindRequestDto.class))).willReturn(List.of(
                RecommendDrugResponseDto.from(Drug.builder()
                                .drugImgPath("img")
                                .url("www.naver")
                                .vendor("vendor")
                                .englishName("english")
                                .name("한글").build()
                        )));

        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(DRUG_RECOMMEND+"/proper2")
                        .content(gson.toJson(DrugFindRequestDto.builder().nutrients(List.of("탄수화물")).build()))
                        .contentType(MediaType.APPLICATION_JSON)
        );


        //then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.data[0].name").value("한글"));


    }



}