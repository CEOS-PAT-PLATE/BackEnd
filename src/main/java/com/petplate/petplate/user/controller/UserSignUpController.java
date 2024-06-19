package com.petplate.petplate.user.controller;

import com.petplate.petplate.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserSignUpController {

    private final UserService userService;



}
