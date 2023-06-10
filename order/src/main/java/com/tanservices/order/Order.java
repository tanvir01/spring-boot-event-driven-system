package com.tanservices.order;

import com.tanservices.order.consumer.ProductOrderInfo;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Document(collection = "order")
public class Order {
    @Id
    private String id;
    private UUID orderId;
    private double totalPrice;
    private LocalDateTime createdAt;
    private List<ProductOrderInfo> productOrderInfos;

    public Order(UUID orderId, double totalPrice, LocalDateTime createdAt, List<ProductOrderInfo> productOrderInfos) {
        this.orderId = orderId;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
        this.productOrderInfos = productOrderInfos;
    }
}
