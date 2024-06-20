package com.petplate.petplate.common.response.error;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCode {

    /**
     * 400 Bad Request
     */
    BAD_REQUEST("잘못된 요청입니다."),


    /**
     * 401 Unauthorized
     */
    UNAUTHORIZE("인증에 실패하였습니다"),

    /**
     * 403 Forbidden
     */


    /**
     * 404 Not Found
     */
    NOT_FOUND("존재하지 않는 값입니다");


    /**
     * 405 Method Not Allowed


     /**
     * 409 Conflict
     */




    /**
     * 500 Internal Server Error
     */






    private final String message;
}