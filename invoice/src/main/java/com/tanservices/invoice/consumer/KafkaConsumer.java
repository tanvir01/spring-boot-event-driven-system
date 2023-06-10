package com.tanservices.invoice.consumer;

import com.tanservices.invoice.InvoiceService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    private final InvoiceService invoiceService;

    public KafkaConsumer(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @KafkaListener(topics = "${kafka-topic-name}", groupId = "invoice_group_id", containerFactory = "messageFactory")
    public void consume(ProductOrderRequest productOrderRequest)
    {
        System.out.println("Listener Received: "+ productOrderRequest);

        invoiceService.createInvoice(productOrderRequest);
    }
}
