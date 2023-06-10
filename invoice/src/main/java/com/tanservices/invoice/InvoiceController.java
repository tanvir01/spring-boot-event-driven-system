package com.tanservices.invoice;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/invoice")
public class InvoiceController {
    private final InvoiceService invoiceService;
    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("/{invoiceId}")
    public ResponseEntity<Invoice> getInvoiceById(@PathVariable UUID invoiceId) {
        return ResponseEntity.ok(invoiceService.getInvoiceById(invoiceId));
    }

    @GetMapping("/{invoiceId}/print")
    public  ResponseEntity<ByteArrayResource> getInvoicePdfById(@PathVariable UUID invoiceId) throws IOException {
        ByteArrayResource resource = invoiceService.getInvoicePdfById(invoiceId);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

}
