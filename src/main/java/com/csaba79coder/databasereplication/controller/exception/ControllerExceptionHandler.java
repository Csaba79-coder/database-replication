package com.csaba79coder.databasereplication.controller.exception;

import com.csaba79coder.databasereplication.value.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.NoSuchElementException;

import static com.csaba79coder.databasereplication.value.ErrorCode.ERROR_CODE_001;
import static com.csaba79coder.databasereplication.value.ErrorCode.ERROR_CODE_002;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(value = {NoSuchElementException.class})
    public ResponseEntity<Object> handleNoSuchElementException(NoSuchElementException ex) {
        return new ResponseEntity<>(responseBodyWithMessage(ERROR_CODE_001, ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {AlreadyExistsException.class})
    public ResponseEntity<Object> handleAlreadyExistsException(AlreadyExistsException ex) {
        return new ResponseEntity<>(responseBodyWithMessage(ERROR_CODE_002, ex.getMessage()), HttpStatus.CONFLICT);
    }

    private String responseBodyWithMessage(ErrorCode code, String message) {
        return Map.of(code, message).toString();
    }
}
