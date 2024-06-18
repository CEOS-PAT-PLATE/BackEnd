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
    BAD_REQUEST("잘못된 요청입니다.");


    /**
     * 401 Unauthorized
     */

    /**
     * 403 Forbidden
     */


    /**
     * 404 Not Found
     */



    /**
     * 405 Method Not Allowed


     /**
     * 409 Conflict
     */




    /**
     * 500 Internal Server Error
     */


    /**
     * 500 Internal Server Error
     */



    private final String message;
}