package com.tanservices.notification.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaConsumer {
    @KafkaListener(topics = "${kafka-topic-name}", groupId = "notification_group_id", containerFactory = "messageFactory")
    public void consume(ProductOrderRequest productOrderRequest)
    {
        log.info("Listener Received: "+ productOrderRequest);

        // Logging Notification For Now. Integration with 3rd-party system can be added to send actual notifications.
        log.info("Order created with id: " + productOrderRequest.orderUUID() +
                " Invoice created with id: " + productOrderRequest.invoiceUUID() +
                " Created At: " + productOrderRequest.createdAt());

    }
}
