package com.petplate.petplate.auth.fortest;

import com.petplate.petplate.auth.interfaces.CurrentUserUsername;
import com.petplate.petplate.common.response.BaseResponse;
import com.petplate.petplate.common.response.error.ErrorCode;
import com.petplate.petplate.common.response.error.exception.NotFoundException;
import com.petplate.petplate.user.domain.entity.User;
import com.petplate.petplate.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SocialLoginAndJwtTestController {

    private final UserRepository userRepository;

    @GetMapping("/test/login")
    //@Operation(summary = "로그인 테스트 확인", description = "로그인 되어 있는지 판단합니다")
    public ResponseEntity<BaseResponse<String>> checkLogin(@CurrentUserUsername String username) {

        User findUser = getUserByUserId(username);

        return ResponseEntity.ok(BaseResponse.createSuccess("로그인 성공을 축하드립니다 당신의 이름은 "+findUser.getName()));
    }

    private User getUserByUserId(final String username){
        return userRepository.findByUsername(username).orElseThrow(()-> new NotFoundException(
                ErrorCode.NOT_FOUND));
    }

}
