package com.tanservices.product;

public record OrderRequest(
        String productId,
        int quantity
) {
}
