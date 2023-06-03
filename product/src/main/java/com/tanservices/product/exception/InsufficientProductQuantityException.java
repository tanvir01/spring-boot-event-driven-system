package com.tanservices.product.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InsufficientProductQuantityException extends RuntimeException {
    public InsufficientProductQuantityException(String productId) {
        super("Product with id: " + productId + " has insufficient quantity");
    }
}
