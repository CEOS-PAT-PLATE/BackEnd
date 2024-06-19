package com.petplate.petplate.common.response.error.exception;


import com.petplate.petplate.common.response.error.ErrorCode;
import lombok.Getter;


@Getter
public class NoSuchElementException extends RuntimeException {

    public NoSuchElementException(ErrorCode code) {
        super(code.getMessage());
    }
}
