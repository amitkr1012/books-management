package com.digicert.books.management.app.exception;

import com.digicert.openApi.model.service.model.ResultStatus;
import jakarta.persistence.OptimisticLockException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class DigiCertExceptionHandler {

    private static final String FAILED="FAILED";

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ResultStatus> handleNotFound(NoSuchElementException ex) {
        ResultStatus rs=new ResultStatus();
        rs.status(FAILED);
        rs.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(rs);
    }

    @ExceptionHandler(OptimisticLockException.class)
    public ResponseEntity<ResultStatus> handleConflict(OptimisticLockException ex) {
        ResultStatus rs=new ResultStatus();
        rs.status(FAILED);
        rs.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(rs);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResultStatus> handleBadRequest(IllegalArgumentException ex) {
        ResultStatus rs=new ResultStatus();
        rs.status(FAILED);
        rs.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(rs);
    }
}
