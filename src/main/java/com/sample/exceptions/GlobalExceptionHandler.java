package com.sample.exceptions;

import java.util.Date;

import com.sample.model.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@Component
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity globalExceptionHandler(Exception ex, WebRequest request) {

    ErrorMessage message = new ErrorMessage(
            new Date(),
            ex.getMessage(),
            request.getDescription(false));
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
  }

  @ExceptionHandler(TimeIntervalConflictedException.class)
  public ResponseEntity timeIntervalConflictedException(TimeIntervalConflictedException ex, WebRequest request) {
    ErrorMessage message = new ErrorMessage(
            new Date(),
            ex.getMessage(),
            request.getDescription(false));
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
  }
}
