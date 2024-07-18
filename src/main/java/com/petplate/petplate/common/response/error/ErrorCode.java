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
    NOT_USER_BOOK_MARK("유저의 즐겨찾기가 아닙니다."),
    NOT_PET_FOOD("해당 반려견의 음식이 아닙니다"),
    NOT_PET_DAILY_MEAL("해당 반려견의 식사 내역이 아닙니다"),
    NUTRIENT_ALREADY_EXIST("이미 오늘의 영양소 분석을 진행하였습니다"),
    ACCESS_TOKEN_NOT_MATCH("엑세스 토큰을 확인해주세요"),
    REFRESH_TOKEN_NOT_MATCH("리프레시 토큰을 확인해주세요"),
    NO_MEMBERSHIP_EXISTS("멤버쉽이 존재하지 않습니다."),



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
    USER_NOT_FOUND("존재하지 않는 유저입니다."),

    NUTRIENT_NOT_FOUND("존재하지 않는 영양소입니다."),

    RAW_NOT_FOUND("존재하지 않는 자연식입니다."),
    BOOK_MARK_NOT_FOUND("존재하지 않은 즐겨찾기입니다."),

    DAILY_MEAL_NOT_FOUND("존재하지 않는 식사 내역입니다."),
    DAILY_RAW_NOT_FOUND("존재하지 않는 자연식 식사 내역입니다."),
    DAILY_FEED_NOT_FOUND("존재하지 않는 사료 식사 내역입니다."),
    DAILY_PACKAGED_SNACK_NOT_FOUND("존재하지 않는 포장 간식 식사 내역입니다"),
    DAILY_BOOK_MARKED_NOT_FOUND("존재하지 않는 즐겨찾기 식사 내역입니다."),

    IMAGE_NOT_FOUND("존재하지 않는 이미지입니다"),

    DRUG_NOT_FOUND("존재하지 않는 약입니다."),


    /**
     * 405 Method Not Allowed


     /**
     * 409 Conflict
     */


    /**
     * 500 Internal Server Error
     */
    DATA_NOT_READY("데이터가 준비되지 않았습니다");





    private final String message;
}