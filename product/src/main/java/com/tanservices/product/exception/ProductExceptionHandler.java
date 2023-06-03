package com.tanservices.product.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ProductExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ProductErrorRespose> handleProductNotFoundException(ProductNotFoundException ex) {
        ProductErrorRespose productErrorRespose = new ProductErrorRespose(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(productErrorRespose);
    }

    @ExceptionHandler(InsufficientProductQuantityException.class)
    public ResponseEntity<ProductErrorRespose> handleInsufficientProductQuantityException(InsufficientProductQuantityException ex) {
        ProductErrorRespose productErrorRespose = new ProductErrorRespose(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(productErrorRespose);
    }
}
