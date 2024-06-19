package com.petplate.petplate.common.response.error.exception;



import com.petplate.petplate.common.response.error.ErrorCode;
import lombok.Getter;


@Getter
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(ErrorCode code) {
        super(code.getMessage());
    }
}
