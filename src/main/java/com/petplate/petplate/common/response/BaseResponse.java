package com.petplate.petplate.common.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BaseResponse<T> {

    private static final String SUCCESS_STATUS = "success";
    private static final String FAIL_STATUS = "fail";
    private static final String ERROR_STATUS = "error";


    private String status;
    private String message;
    private T data;

    public static <T> BaseResponse<T> createSuccess(T data) {
        return new BaseResponse<>(SUCCESS_STATUS, data, null);
    }

    public static BaseResponse<?> createSuccessWithNoContent() {
        return new BaseResponse<>(SUCCESS_STATUS, null, null);
    }


    // 예외 발생으로 API 호출 실패시 반환
    public static BaseResponse<?> createError(String message) {
        return new BaseResponse<>(ERROR_STATUS, null, message);
    }

    private BaseResponse(String status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }


}
