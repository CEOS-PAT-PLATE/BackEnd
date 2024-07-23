package com.petplate.petplate.common.response.error.exception;


import com.petplate.petplate.common.response.error.ErrorCode;
import lombok.Getter;


@Getter
public class NotFoundException extends RuntimeException {

    public NotFoundException(ErrorCode code) {
        super(code.getMessage());
    }
}
