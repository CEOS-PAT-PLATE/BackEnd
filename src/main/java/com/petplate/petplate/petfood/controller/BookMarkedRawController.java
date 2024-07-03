package com.petplate.petplate.petfood.controller;

import com.petplate.petplate.auth.interfaces.CurrentUserUsername;
import com.petplate.petplate.common.response.BaseResponse;
import com.petplate.petplate.petdailymeal.dto.request.CreateDailyBookMarkedRawRequestDto;
import com.petplate.petplate.petdailymeal.service.DailyBookMarkedRawService;
import com.petplate.petplate.petfood.dto.request.CreateBookMarkedRawRequestDto;
import com.petplate.petplate.petfood.dto.response.ReadBookMarkedRawResponseDto;
import com.petplate.petplate.petfood.service.BookMarkedRawService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "즐겨찾기한 자연식 컨트롤러", description = "자연식(즐겨찾기) 및 펫이 하룻 동안 섭취한 자연식(즐겨찾기)에 대한 컨트롤러 입니다")
public class BookMarkedRawController {

    private final DailyBookMarkedRawService dailyBookMarkedRawService;
    private final BookMarkedRawService bookMarkedRawService;

    @PostMapping("/bookmark/raws")
    public ResponseEntity<BaseResponse> createBookMarkedRaws(@CurrentUserUsername String username, @Valid CreateBookMarkedRawRequestDto requestDto) {
        Long bookMarkedRawId = bookMarkedRawService.createBookMarkedRaw(username, requestDto);

        return new ResponseEntity(BaseResponse.createSuccess(bookMarkedRawId), HttpStatus.CREATED);
    }

    @GetMapping("/bookmark/raws")
    public ResponseEntity<BaseResponse> readBookMarkedRaws(@CurrentUserUsername String username) {
        List<ReadBookMarkedRawResponseDto> bookMarkedRaws = bookMarkedRawService.getBookMarkedRaws(username);

        return new ResponseEntity(BaseResponse.createSuccess(bookMarkedRaws), HttpStatus.OK);
    }

    @GetMapping("/bookmark/raws/{bookMarkedRawId}")
    public ResponseEntity<BaseResponse> readBookMarkedRaws(@CurrentUserUsername String username, @PathVariable Long bookMarkedRawId) {
        ReadBookMarkedRawResponseDto bookMarkedRaw = bookMarkedRawService.getBookMarkedRaw(username, bookMarkedRawId);

        return new ResponseEntity(BaseResponse.createSuccess(bookMarkedRaw), HttpStatus.OK);
    }

    @DeleteMapping("/bookmark/raws/{bookMarkedRawId}")
    public ResponseEntity<BaseResponse> deleteBookMarkedRaw(@CurrentUserUsername String username, @PathVariable Long bookMarkedRawId) {
        bookMarkedRawService.deleteBookMarkedRaw(username, bookMarkedRawId);

        return new ResponseEntity(BaseResponse.createSuccess(null), HttpStatus.OK);
    }

    @PostMapping("/pet/{petId}/dailyMeals/bookmark/raws")
    public ResponseEntity<BaseResponse> createDailyBookMarkRaw(@CurrentUserUsername String username, @PathVariable Long petId, @Valid CreateDailyBookMarkedRawRequestDto requestDto) {
        Long id = dailyBookMarkedRawService.createDailyBookMarkedRaw(username, petId, requestDto);

        return new ResponseEntity(BaseResponse.createSuccess(id), HttpStatus.CREATED);
    }

    @GetMapping("/pet/{petId}/dailyMeals/{dailyMealId}/bookmark/raws")
    public ResponseEntity<BaseResponse> getBookMarkedRaws(@CurrentUserUsername String username,@PathVariable Long petId, @PathVariable Long dailyMealId) {
        List<ReadBookMarkedRawResponseDto> bookMarkedRaws = dailyBookMarkedRawService.getBookMarkedRaws(username, petId, dailyMealId);

        return new ResponseEntity(BaseResponse.createSuccess(bookMarkedRaws), HttpStatus.OK);
    }

    @DeleteMapping("/pet/{petId}/dailyMeals/bookmark/raws/{dailyBookMarkedRawsId}")
    public ResponseEntity<BaseResponse> deleteDailyBookMarkedRaw(@CurrentUserUsername String username, @PathVariable Long petId, @PathVariable Long dailyBookMarkedRawsId) {
        dailyBookMarkedRawService.deleteDailyBookMarkedRaw(username, petId, dailyBookMarkedRawsId);

        return new ResponseEntity(BaseResponse.createSuccess(null), HttpStatus.OK);
    }
}
