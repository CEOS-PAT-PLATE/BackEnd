package com.petplate.petplate.user.controller;

import com.petplate.petplate.auth.interfaces.CurrentUserUsername;
import com.petplate.petplate.common.response.BaseResponse;
import com.petplate.petplate.user.dto.request.SecretCodeRequestDto;
import com.petplate.petplate.user.dto.response.MyProfileResponseDto;
import com.petplate.petplate.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "유저 관련 컨트롤러", description = "유저와 관련된 모든 활동을 모아 놓는 컨트롤러입니다.")
public class UserController {

    private final UserService userService;


    @GetMapping("/my-profile")
    @Operation(summary = "자신의 프로필 조회",description = "자신의 프로필을 조회합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "자신의 프로필 성공적 반환"),
            @ApiResponse(responseCode = "404",description = "로그인이 풀려서 ID 기반으로 유저를 찾아올 때 없을 때")
    })
    public ResponseEntity<BaseResponse<MyProfileResponseDto>> showMyProfile(@CurrentUserUsername String username){

        return ResponseEntity.ok(BaseResponse.createSuccess(userService.showMyProfile(username)));

    }

    @DeleteMapping("/delete")
    @Operation(summary = "회원 탈퇴",description = "회원탈퇴를 진행합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "회원 탈퇴 성공"),
            @ApiResponse(responseCode = "400",description = "refreshToken 이 만료되거나 틀렸을 때")
    })
    public ResponseEntity<Void> deleteUser(@CurrentUserUsername String username){

        userService.deleteUser(username);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/authority")
    @Operation(summary = "회원 권한 변경",description = "회원 권한을 변경합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "회원 권한  변경 성공"),
            @ApiResponse(responseCode = "400",description = "회원 권한 변경 비밀번호 불일치"),
    })
    public ResponseEntity<Void> changeRole(@CurrentUserUsername String username,final @RequestBody @Valid
            SecretCodeRequestDto secretCodeRequestDto){

        userService.changeRole(username,secretCodeRequestDto);

        return ResponseEntity.ok().build();
    }


}
