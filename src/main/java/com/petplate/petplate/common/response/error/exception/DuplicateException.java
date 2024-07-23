package com.petplate.petplate.common.response.error.exception;



import com.petplate.petplate.common.response.error.ErrorCode;
import lombok.Getter;


@Getter
public class DuplicateException extends RuntimeException {

    public DuplicateException(ErrorCode code) {
        super(code.getMessage());
    }
}
