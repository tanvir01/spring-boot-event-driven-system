package com.tanservices.invoice.consumer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ProductOrderRequest(
        List<ProductOrderInfo> productOrderInfos,
        UUID orderUUID,
        UUID invoiceUUID,
        LocalDateTime createdAt
) {
    public ProductOrderRequest(List<ProductOrderInfo> productOrderInfos) {
        this(productOrderInfos, UUID.randomUUID(), UUID.randomUUID(), LocalDateTime.now());
    }
}
