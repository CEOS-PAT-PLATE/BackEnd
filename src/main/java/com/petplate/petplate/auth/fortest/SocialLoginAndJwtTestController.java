package com.petplate.petplate.auth.fortest;

import com.petplate.petplate.auth.interfaces.CurrentUserUsername;
import com.petplate.petplate.common.response.BaseResponse;
import com.petplate.petplate.common.response.error.ErrorCode;
import com.petplate.petplate.common.response.error.exception.NotFoundException;
import com.petplate.petplate.user.domain.entity.User;
import com.petplate.petplate.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "소셜 로그인 테스트 컨트롤러", description = "임시로 소셜 로그인 인가 처리를 확인하는 테스트 컨트롤러입니다. 이 컨트롤러를 이용할 일은 없습니다.")
public class SocialLoginAndJwtTestController {

    private final UserRepository userRepository;

    @GetMapping("/test/login")
    @Operation(summary = "로그인 테스트 확인",description = "로그인을 확인합니다. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",description = "영양제 성공적 저장"),
            @ApiResponse(responseCode = "403",description = "권한 없음"),
            @ApiResponse(responseCode = "401",description = "인가 실패")
    })
    public ResponseEntity<BaseResponse<String>> checkLogin(@CurrentUserUsername String username) {

        User findUser = getUserByUserId(username);

        return ResponseEntity.ok(BaseResponse.createSuccess("로그인 성공을 축하드립니다 당신의 이름은 "+findUser.getName()));
    }

    private User getUserByUserId(final String username){
        return userRepository.findByUsername(username).orElseThrow(()-> new NotFoundException(ErrorCode.NOT_FOUND));
    }

}
