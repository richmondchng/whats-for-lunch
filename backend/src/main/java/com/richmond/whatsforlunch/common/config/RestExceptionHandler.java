package com.richmond.whatsforlunch.common.config;

import com.richmond.whatsforlunch.common.controller.StandardErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Generic error handling for controller exceptions.
     *
     * @param ex                 exception
     * @param httpServletRequest HTTP servlet request
     * @param webRequest         Web Request
     * @return ResponseEntity
     */
    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<StandardErrorResponse> handleStandardController4xxException(
            final RuntimeException ex, final HttpServletRequest httpServletRequest,
            final WebRequest webRequest) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new StandardErrorResponse(LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        ex.getMessage(), httpServletRequest.getRequestURI()));

    }
}


