package com.petplate.petplate.common.response.error.exception;



import com.petplate.petplate.common.response.error.ErrorCode;
import lombok.Getter;


@Getter
public class InternalServerErrorException extends RuntimeException {

    public InternalServerErrorException(ErrorCode code) {
        super(code.getMessage());
    }
}
