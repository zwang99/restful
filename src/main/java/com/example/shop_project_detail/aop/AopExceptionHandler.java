package com.example.shop_project_detail.aop;

import com.example.shop_project_detail.domain.response.ErrorResponse;
import com.example.shop_project_detail.exception.InvalidCredentialsException;
import com.example.shop_project_detail.exception.NotEnoughInventoryException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AopExceptionHandler {

    @ExceptionHandler(value = {NotEnoughInventoryException.class})
    public ResponseEntity handleNotEnoughInventory(NotEnoughInventoryException e){
        return new ResponseEntity(ErrorResponse.builder().message(e.getMessage()).build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {InvalidCredentialsException.class})
    public ResponseEntity handleInvalidCredentialsException(InvalidCredentialsException e){
        return new ResponseEntity(ErrorResponse.builder().message(e.getMessage()).build(), HttpStatus.BAD_REQUEST);
    }
}
