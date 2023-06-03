package com.tanservices.product;

public record ProductOrderRequest(
        String productId,
        int quantity
) {
}
