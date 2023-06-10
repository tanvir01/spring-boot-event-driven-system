package com.tanservices.invoice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class InvoiceExceptionHandler {

    @ExceptionHandler(InvoiceNotFoundException.class)
    public ResponseEntity<InvoiceErrorRespose> handleInvoiceNotFoundException(InvoiceNotFoundException ex) {
        InvoiceErrorRespose invoiceErrorRespose = new InvoiceErrorRespose(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(invoiceErrorRespose);
    }
}
