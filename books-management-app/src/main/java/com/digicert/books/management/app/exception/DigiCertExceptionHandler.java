package com.digicert.books.management.app.exception;

import com.digicert.openApi.model.service.model.CommonErrorResponse;
import jakarta.persistence.OptimisticLockException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class DigiCertExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<CommonErrorResponse> handleNotFound(NoSuchElementException ex) {
        CommonErrorResponse cr=new CommonErrorResponse();
        cr.setErrorCode(404);
        cr.setErrorMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(cr);
    }

    @ExceptionHandler(OptimisticLockException.class)
    public ResponseEntity<CommonErrorResponse> handleConflict(OptimisticLockException ex) {
        CommonErrorResponse cr=new CommonErrorResponse();
        cr.setErrorMessage(ex.getMessage());
        cr.setErrorCode(412);
        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(cr);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CommonErrorResponse> handleBadRequest(IllegalArgumentException ex) {
        CommonErrorResponse cr =new CommonErrorResponse();
        cr.setErrorCode(400);
        cr.errorMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(cr);
    }
}
