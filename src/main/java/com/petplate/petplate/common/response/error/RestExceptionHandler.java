package com.petplate.petplate.common.response.error;


import com.petplate.petplate.common.response.BaseResponse;
import com.petplate.petplate.common.response.error.exception.BadRequestException;
import com.petplate.petplate.common.response.error.exception.DuplicateException;
import com.petplate.petplate.common.response.error.exception.ForbiddenException;
import com.petplate.petplate.common.response.error.exception.InternalServerErrorException;
import com.petplate.petplate.common.response.error.exception.NoSuchElementException;
import com.petplate.petplate.common.response.error.exception.NotFoundException;
import com.petplate.petplate.common.response.error.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.format.DateTimeParseException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    private static final String DATE_TIME_FORMAT = "날짜 형식이 올바른지 확인해주세요";

    // Custom Bad Request Error
    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<BaseResponse<?>> handleBadRequestException(
            BadRequestException exception,
            HttpServletRequest request) {
        logInfo(request, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponse.createError(
                exception.getMessage()));
    }

    // Custom Unauthorized Error

    @ExceptionHandler(UnauthorizedException.class)
    protected ResponseEntity<BaseResponse<?>> handleUnauthorizedException(
            UnauthorizedException exception,
            HttpServletRequest request) {
        logInfo(request, exception.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(BaseResponse.createError(
                exception.getMessage()));
    }

    // Custom Internal Server Error

    @ExceptionHandler(InternalServerErrorException.class)
    protected ResponseEntity<BaseResponse<?>> handleInternalServerErrorException(
            InternalServerErrorException exception,
            HttpServletRequest request) {
        logInfo(request, exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(BaseResponse.createError(
                        exception.getMessage()));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<BaseResponse<?>> handleMethodArgNotValidException(
            MethodArgumentNotValidException exception,
            HttpServletRequest request) {
        String message = exception.getBindingResult().getFieldError().getDefaultMessage();

        StringBuilder builder = new StringBuilder();
        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            builder.append("[");
            builder.append(fieldError.getField());
            builder.append("](은)는 ");
            builder.append(fieldError.getDefaultMessage());
            builder.append(" 입력된 값: [");
            builder.append(fieldError.getRejectedValue());
            builder.append("]");
        }

        logInfo(request, message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(BaseResponse.createError(builder.toString()));
    }


    @ExceptionHandler(BindException.class)
    protected ResponseEntity<BaseResponse<?>> handleMethodArgNotValidException(
            BindException exception,
            HttpServletRequest request) {
        String message = exception.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        logInfo(request, message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(BaseResponse.createError(exception.getMessage()));
    }


    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<BaseResponse<?>> handleNotFoundException(NotFoundException exception,
            HttpServletRequest request) {
        logInfo(request, exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(BaseResponse.createError(exception.getMessage()));
    }


    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<BaseResponse<?>> handleDuplicationException(DuplicateException exception,
            HttpServletRequest request) {
        logInfo(request, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(BaseResponse.createError(exception.getMessage()));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<BaseResponse<?>> handlerForbiddenException(ForbiddenException exception,
            HttpServletRequest request) {
        logInfo(request, exception.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(BaseResponse.createError(exception.getMessage()));
    }


    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<BaseResponse<?>> handlerNoSuchElementException(
            ForbiddenException exception,
            HttpServletRequest request) {
        logInfo(request, exception.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(BaseResponse.createError(exception.getMessage()));
    }


    @ExceptionHandler(InterruptedException.class)
    public ResponseEntity<BaseResponse<?>> handlerNoSuchElementException(
            InterruptedException exception,
            HttpServletRequest request) {
        logInfo(request, exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(BaseResponse.createError(exception.getMessage()));
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<BaseResponse<?>> handleTypeMismatchDateTimeFormatExceptions(DateTimeParseException ex){


        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponse.createError(DATE_TIME_FORMAT));
    }


    private void logInfo(HttpServletRequest request, String message) {
        log.info("{} {} : {} (traceId: {})",
                request.getMethod(), request.getRequestURI(), message, getTraceId());
    }


    private String getTraceId() {
        return MDC.get("traceId");
    }
}
