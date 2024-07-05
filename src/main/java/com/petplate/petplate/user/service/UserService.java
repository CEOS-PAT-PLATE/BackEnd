package com.petplate.petplate.user.service;

import com.petplate.petplate.common.response.error.ErrorCode;
import com.petplate.petplate.common.response.error.exception.NotFoundException;
import com.petplate.petplate.user.domain.entity.User;
import com.petplate.petplate.user.dto.response.MyProfileResponseDto;
import com.petplate.petplate.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public MyProfileResponseDto showMyProfile(String username){

        User findUser = findUserByUsername(username);

        return MyProfileResponseDto.from(findUser);

    }

    private User findUserByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow(()->new NotFoundException(
                ErrorCode.USER_NOT_FOUND));
    }


}
