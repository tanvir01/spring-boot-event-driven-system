package com.tanservices.order.consumer;

import com.tanservices.order.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaConsumer {

    private final OrderService orderService;

    public KafkaConsumer(OrderService orderService) {
        this.orderService = orderService;
    }

    @KafkaListener(topics = "${kafka-topic-name}", groupId = "order_group_id", containerFactory = "messageFactory")
    public void consume(ProductOrderRequest productOrderRequest)
    {
        log.info("Listener Received: "+ productOrderRequest);

        orderService.createOrder(productOrderRequest);
    }
}
