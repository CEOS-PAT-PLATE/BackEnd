package com.petplate.petplate.auth.controller;

import com.petplate.petplate.auth.dto.response.AuthResponseWithTokenAndRedirectUserInfo;
import com.petplate.petplate.auth.dto.response.UserEnrollResponseDto;
import com.petplate.petplate.auth.interfaces.CurrentUserUsername;
import com.petplate.petplate.auth.oauth.Dto.TokenDto;
import com.petplate.petplate.auth.service.AuthService;
import com.petplate.petplate.common.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "인증 관련 컨트롤러", description = "인증과 관련된 모든 활동을 모아 놓는 컨트롤러입니다.")
public class AuthController {

    private final AuthService authService;
    private static final String accessTokenHeader = "Authorization";
    private static final String refreshTokenHeader = "refreshToken";



    @PostMapping("/logout")
    @Operation(summary = "로그아웃",description = "로그아웃을 한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "로그아웃 성공"),
            @ApiResponse(responseCode = "400",description = "accessToken 이 만료되거나 틀렸을 때")
    })
    public ResponseEntity<Void> logout(@RequestHeader("accessToken") final String accessToken
    ){
        authService.logout(accessToken);
        return ResponseEntity.status(HttpStatus.OK).build();
    }




    @PostMapping("/reissue")
    @Operation(summary = "토큰 재발행",description = "accessToken 의 만료로 지정된 자원에 접근할 수 없을 떄 사용, refreshToken 을 기반으로 accessToken 재발행")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "토큰 재발행 성공"),
            @ApiResponse(responseCode = "400",description = "refreshToken 이 만료되거나 틀렸을 때")
    })
    public ResponseEntity<Void> reIssue(@RequestHeader("accessToken") final String accessToken,
            @RequestHeader("refreshToken") final String refreshToken){

        TokenDto tokenDto = authService.reIssue(accessToken,refreshToken);


        return ResponseEntity.status(HttpStatus.OK)
                .header(accessTokenHeader,tokenDto.getAccessToken())
                .header(refreshTokenHeader,tokenDto.getRefreshToken())
                .build();

    }


    @GetMapping("/issue")
    @Operation(summary = "토큰 발행",description = "code 를 기반으로 토큰을 발행한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "토큰 재발행 성공")
    })
    public ResponseEntity<BaseResponse<UserEnrollResponseDto>> getToken(@RequestParam("code") final String code){

        AuthResponseWithTokenAndRedirectUserInfo authResponseWithTokenAndRedirectUserInfo  = authService.getTokenByCode(code);

        return ResponseEntity.status(HttpStatus.OK)
                .header(accessTokenHeader,authResponseWithTokenAndRedirectUserInfo.getTokenDto().getAccessToken())
                .header(refreshTokenHeader,authResponseWithTokenAndRedirectUserInfo.getTokenDto().getRefreshToken())
                .body(BaseResponse.createSuccess(authResponseWithTokenAndRedirectUserInfo.getUserEnrollResponseDto()));

    }

    @GetMapping("/test")
    public ResponseEntity<BaseResponse<Long>> getUserInfoTest(@RequestParam("username") String username){


        return ResponseEntity.ok().body(BaseResponse.createSuccess(authService.getMemberId(username)));

    }

}
