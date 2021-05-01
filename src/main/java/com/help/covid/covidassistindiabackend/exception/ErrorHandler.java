package com.help.covid.covidassistindiabackend.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ErrorHandler {

    private final String genericMessage = "Exception while processing request : {}";
    public static final String BAD_REQUEST_ERROR = "ORDERS_BAD_REQUEST";


    @ExceptionHandler({BadRequest.class})
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse sendErrorResponseForBadRequest(Exception e) {
        log.error(genericMessage, e.getMessage());
        return createErrorResponse(BAD_REQUEST_ERROR, e.getMessage());
    }

    private ErrorResponse createErrorResponse(String code, String message) {
        return ErrorResponse.builder()
                .error(code)
                .message(message)
                .build();
    }

}
