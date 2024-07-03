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
    NOT_USER_PET("유저의 반려견이 아닙니다."),
    RAW_ALREADY_EXISTS("이미 존재하는 자연식입니다."),
    BOOK_MARK_ALREADY_EXISTS("이미 즐겨찾기가 존재합니다."),


    /**
     * 401 Unauthorized
     */
    UNAUTHORIZE("인증에 실패하였습니다."),

    /**
     * 403 Forbidden
     */


    /**
     * 404 Not Found
     */
    NOT_FOUND("존재하지 않는 값입니다."),
    PET_NOT_FOUND("존재하지 않는 반려견입니다."),

    NUTRIENT_NOT_FOUND("존재하지 않는 영양소입니다."),

    RAW_NOT_FOUND("존재하지 않는 자연식입니다."),
    BOOK_MARK_NOT_FOUND("존재하지 않은 즐겨찾기입니다."),

    DAILY_MEAL_NOT_FOUND("존재하지 않는 식사 내역입니다."),
    DAILY_RAW_NOT_FOUND("존재하지 않는 자연식 식사 내역입니다."),
    DAILY_BOOK_MARKED_NOT_FOUND("존재하지 않는 즐겨찾기 식사 내역입니다."),

    DRUG_NOT_FOUND("존재하지 않는 약입니다.");


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