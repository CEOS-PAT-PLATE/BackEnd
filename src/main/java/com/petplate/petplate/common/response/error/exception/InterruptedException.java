package com.petplate.petplate.common.response.error.exception;



import com.petplate.petplate.common.response.error.ErrorCode;
import lombok.Getter;


@Getter
public class InterruptedException extends RuntimeException {

    public InterruptedException(ErrorCode code) {
        super(code.getMessage());
    }
}
