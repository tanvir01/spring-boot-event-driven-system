package com.tanservices.order.consumer;

import com.tanservices.order.OrderService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    private final OrderService orderService;

    public KafkaConsumer(OrderService orderService) {
        this.orderService = orderService;
    }

    @KafkaListener(topics = "${kafka-topic-name}", groupId = "group_id", containerFactory = "messageFactory")
    public void consume(ProductOrderRequest productOrderRequest)
    {
        System.out.println("Listener Received: "+ productOrderRequest);

        orderService.createOrder(productOrderRequest);
    }
}
