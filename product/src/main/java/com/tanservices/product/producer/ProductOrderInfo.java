package com.tanservices.product.producer;

import com.tanservices.product.Product;

public record ProductOrderInfo(Product product, int requestQuantity) {
}
