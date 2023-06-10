package com.tanservices.order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface OrderRepository extends MongoRepository<Order, String> {
    Order findByOrderId(UUID orderId);
}

