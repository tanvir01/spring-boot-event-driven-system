package com.tanservices.invoice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InvoiceNotFoundException extends RuntimeException {
    public InvoiceNotFoundException(UUID invoiceId) {
        super("Invoice not found with UUID: " + invoiceId);
    }
}
