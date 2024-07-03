package com.petplate.petplate.petfood.controller;

import com.petplate.petplate.common.response.BaseResponse;
import com.petplate.petplate.petfood.dto.request.CreateRawRequestDto;
import com.petplate.petplate.petfood.dto.response.ReadRawResponseDto;
import com.petplate.petplate.petfood.service.RawService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@Tag(name = "자연식 컨트롤러", description = "자연식에 대한 컨트롤러입니다")
public class RawController {
    private final RawService rawService;

    @Operation(summary = "자연식 추가")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "자연식 성공적 추가"),
            @ApiResponse(responseCode = "400", description = "동명의 자연식이 존재")
    })
    @PostMapping("/raws")
    public ResponseEntity<BaseResponse> createRaw(@Valid CreateRawRequestDto requestDto) {
        Long rawId = rawService.createRaw(requestDto);

        return new ResponseEntity<>(BaseResponse.createSuccess(rawId), HttpStatus.OK);
    }

    @Operation(summary = "rawId로 자연식 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "자연식 성공적 조회"),
            @ApiResponse(responseCode = "404", description = "존재하지 않은 자연식")
    })
    @GetMapping("/raws/{rawId}")
    public ResponseEntity<BaseResponse> readRaw(@PathVariable Long rawId) {
        ReadRawResponseDto response = rawService.getRaw(rawId);

        return new ResponseEntity<>(BaseResponse.createSuccess(response), HttpStatus.OK);
    }

    @Operation(summary = "keyword가 포함된 자연식 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "자연식 성공적 조회")
    })
    @GetMapping("/raws")
    public ResponseEntity<BaseResponse> readRawByKeyword(@RequestParam String keyword) {
        List<ReadRawResponseDto> responses = rawService.getRawByKeyword(keyword);

        return new ResponseEntity<>(BaseResponse.createSuccess(responses), HttpStatus.OK);
    }

    @Operation(summary = "rawId로 자연식 제거",description = "자연식이 제거된 경우, 해당 자연식을 참조하고 있던 즐겨찾기 자연식과 하루식사 자연식은 '존재하지 않은 정보입니다'로 표기됨")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "자연식 성공적 조회"),
            @ApiResponse(responseCode = "404", description = "존재하지 않은 자연식")
    })
    @GetMapping("/raws")
    public ResponseEntity<BaseResponse> deleteRaw(@RequestParam Long rawId) {
        rawService.deleteRawById(rawId);

        return new ResponseEntity<>(BaseResponse.createSuccess(null), HttpStatus.OK);
    }
}
