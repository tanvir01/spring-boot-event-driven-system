package com.tanservices.order;

import com.tanservices.order.consumer.ProductOrderInfo;
import com.tanservices.order.consumer.Product;
import com.tanservices.order.consumer.ProductOrderRequest;
import com.tanservices.order.exception.OrderNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class OrderServiceIntegrationTest {

    @Autowired
    private OrderService orderService;

    @MockBean
    private OrderRepository orderRepository;

    private UUID orderId;

    @BeforeEach
    public void setup() {
        // Prepare test data
        orderId = UUID.randomUUID();
    }

    @Test
    public void testGetOrderById_OrderExists_ReturnsOrder() {
        Order mockOrder = new Order(orderId, 50.0, LocalDateTime.now(), new ArrayList<>());
        when(orderRepository.findByOrderId(orderId)).thenReturn(mockOrder);

        Order result = orderService.getOrderById(orderId);

        // Verify the result
        Assertions.assertEquals(mockOrder, result);

        // Verify that the order repository method was called
        verify(orderRepository, times(1)).findByOrderId(orderId);
    }

    @Test
    public void testGetOrderById_OrderNotFound_ThrowsOrderNotFoundException() {
        when(orderRepository.findByOrderId(orderId)).thenReturn(null);

        Assertions.assertThrows(OrderNotFoundException.class, () -> {
            orderService.getOrderById(orderId);
        });

        // Verify that the order repository method was called
        verify(orderRepository, times(1)).findByOrderId(orderId);
    }

    @Test
    public void testCreateOrder_ValidProductOrderRequest_SavesOrder() {
        List<ProductOrderInfo> productOrderInfos = new ArrayList<>();
        productOrderInfos.add(new ProductOrderInfo(new Product("productId1", "Product 1", "This is dummy product1", 10.0, 20), 2));
        productOrderInfos.add(new ProductOrderInfo(new Product("productId2", "Product 2", "This is dummy product2", 15.0, 30), 3));

        ProductOrderRequest productOrderRequest = new ProductOrderRequest(productOrderInfos, orderId, UUID.randomUUID(), LocalDateTime.now());

        orderService.createOrder(productOrderRequest);

        // Verify that the order repository save method was called
        verify(orderRepository, times(1)).save(any(Order.class));
    }
}
