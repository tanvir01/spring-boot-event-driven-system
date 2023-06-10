package com.tanservices.invoice;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface InvoiceRepository extends MongoRepository<Invoice, String> {
    Invoice findByInvoiceId(UUID invoiceId);
}
