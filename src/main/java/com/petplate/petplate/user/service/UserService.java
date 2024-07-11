package com.petplate.petplate.user.service;

import com.petplate.petplate.auth.oauth.service.SocialLoginTokenUtil;
import com.petplate.petplate.common.response.error.ErrorCode;
import com.petplate.petplate.common.response.error.exception.BadRequestException;
import com.petplate.petplate.common.response.error.exception.NotFoundException;
import com.petplate.petplate.user.domain.entity.User;
import com.petplate.petplate.user.dto.request.SecretCodeRequestDto;
import com.petplate.petplate.user.dto.response.MyProfileResponseDto;
import com.petplate.petplate.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final SocialLoginTokenUtil socialLoginTokenUtil;

    @Value("${manager.secretCode}")
    private String secretCode;

    public MyProfileResponseDto showMyProfile(String username){

        User findUser = findUserByUsername(username);

        return MyProfileResponseDto.from(findUser);

    }

    @Transactional
    public void deleteUser(final String username){

        socialLoginTokenUtil.unlinkNaver(username);

        User findUser = findUserByUsername(username);
        userRepository.delete(findUser);

    }

    @Transactional
    public void changeRole(final String username,final SecretCodeRequestDto secretCodeRequestDto){

        User findUser = findUserByUsername(username);

        if(!secretCode.equals(secretCodeRequestDto.getSecretCode())){
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        findUser.changeMyRole();
    }



    private User findUserByUsername(String username){

        return userRepository.findByUsername(username).orElseThrow(()->new NotFoundException(
                ErrorCode.USER_NOT_FOUND));
    }









}
