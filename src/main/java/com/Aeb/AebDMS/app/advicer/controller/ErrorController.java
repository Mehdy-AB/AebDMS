package com.Aeb.AebDMS.app.advicer.controller;


import com.Aeb.AebDMS.app.advicer.dto.ErrorDto;
import com.Aeb.AebDMS.app.advicer.exceptions.BaseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@ControllerAdvice
@Slf4j
public class ErrorController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("Caught MethodArgumentNotValidException", ex);

        String errorMessage = ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ErrorDto errorDto = ErrorDto.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(errorMessage)
                .build();

        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDto> handleJsonParse(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();
        if (cause instanceof MismatchedInputException mie) {
            String field = mie.getPath().stream()
                    .map(JsonMappingException.Reference::getFieldName)
                    .filter(Objects::nonNull)
                    .collect(Collectors.joining("."));
            String msg = String.format("Field '%s' expected type %s but got wrong format",
                    field, mie.getTargetType().getSimpleName());
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorDto(HttpStatus.BAD_REQUEST.value(), msg));
        }
        // fallback
        return ResponseEntity
                .badRequest()
                .body(new ErrorDto(HttpStatus.BAD_REQUEST.value(),
                        "Malformed JSON or invalid types"));
    }

    // Catch-all for unexpected exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception ex) {
        log.error("Caught unexpected exception", ex);

        ErrorDto error = ErrorDto.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("An unexpected error occurred")
                .build();

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorDto> handleException(AuthorizationDeniedException ex) {
        log.error("Caught Authorization Denied exception", ex);

        ErrorDto error = ErrorDto.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .message("Access Denied")
                .build();

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorDto> handleCustomException(BaseException ex) {
        log.error("Caught handleCustomException :{}", ex.getMessage());

        ErrorDto errorDto = ErrorDto.builder()
                .status(ex.getStatus().value())
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(errorDto, ex.getStatus());
    }
}
