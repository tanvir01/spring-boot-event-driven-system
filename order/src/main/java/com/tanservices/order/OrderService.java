package com.tanservices.order;

import com.tanservices.order.consumer.ProductOrderInfo;
import com.tanservices.order.consumer.ProductOrderRequest;
import com.tanservices.order.exception.OrderNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order findByOrderId(UUID orderId) {
        Optional<Order> optionalOrder = Optional.ofNullable(orderRepository.findByOrderId(orderId));
        return optionalOrder.orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    public void createOrder(ProductOrderRequest productOrderRequest) {
        List<ProductOrderInfo> productOrderInfos = productOrderRequest.productOrderInfos();
        UUID orderUUID = productOrderRequest.orderUUID();
        UUID invoiceUUID = productOrderRequest.invoiceUUID();
        LocalDateTime createdAt = productOrderRequest.createdAt();

        double totalPrice = productOrderInfos.stream()
                .mapToDouble(productOrderInfo -> productOrderInfo.requestQuantity() * productOrderInfo.product().price())
                .sum();

        orderRepository.save(new Order(orderUUID, totalPrice, createdAt, productOrderInfos));
    }
}
