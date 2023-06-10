package com.tanservices.order.consumer;

public record Product(
        String id,
        String name,
        String description,
        double price,
        int quantity
) {
}
