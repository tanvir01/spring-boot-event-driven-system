package com.tanservices.invoice;

import com.tanservices.invoice.consumer.ProductOrderInfo;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Document(collection = "invoice")
public class Invoice {

    @Id
    private String id;
    private UUID invoiceId;
    private UUID orderId;
    private double totalPrice;
    private LocalDateTime createdAt;
    private List<ProductOrderInfo> productOrderInfos;

    public Invoice(UUID invoiceId, UUID orderId, double totalPrice, LocalDateTime createdAt, List<ProductOrderInfo> productOrderInfos) {
        this.invoiceId = invoiceId;
        this.orderId = orderId;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
        this.productOrderInfos = productOrderInfos;
    }
}
