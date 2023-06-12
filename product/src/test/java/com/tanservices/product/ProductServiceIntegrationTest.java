package com.tanservices.product;

import com.tanservices.product.exception.InsufficientProductQuantityException;
import com.tanservices.product.exception.ProductNotFoundException;
import com.tanservices.product.producer.ProductOrderInfo;
import com.tanservices.product.producer.ProductOrderRequest;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class ProductServiceIntegrationTest extends BaseTest{

    @MockBean
    private KafkaTemplate<String, ProductOrderRequest> kafkaTemplate;

    @MockBean
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @BeforeEach
    public void setUp() {
        Product mockProduct1 = new Product();
        mockProduct1.setId("productId1");
        mockProduct1.setName("Dummy Product");
        mockProduct1.setQuantity(100);
        mockProduct1.setPrice(10);

        Product mockProduct2 = new Product();
        mockProduct2.setId("productId2");
        mockProduct2.setName("Sample Product");
        mockProduct2.setQuantity(200);
        mockProduct2.setPrice(20);

        when(productRepository.findById("productId1")).thenReturn(Optional.of(mockProduct1));
        when(productRepository.findById("productId2")).thenReturn(Optional.of(mockProduct2));
    }

    @Test
    @Order(1)
    public void testPlaceOrder_ValidOrder_ReturnsProductOrderRequest() throws Exception {
        OrderRequest[] orderRequests = {
                new OrderRequest("productId1", 2),
                new OrderRequest("productId2", 3)
        };

        ProductOrderRequest productOrderRequest = productService.placeOrder(orderRequests);

        // Verify the result
        assertNotNull(productOrderRequest);
        assertEquals(orderRequests.length, productOrderRequest.productOrderInfos().size());

        // Verify each product order info in the returned ProductOrderRequest
        List<ProductOrderInfo> productOrderInfos = productOrderRequest.productOrderInfos();
        for (int i = 0; i < orderRequests.length; i++) {
            OrderRequest orderRequest = orderRequests[i];
            ProductOrderInfo productOrderInfo = productOrderInfos.get(i);

            assertNotNull(productOrderInfo);
            assertEquals(orderRequest.productId(), productOrderInfo.product().getId());
            assertEquals(orderRequest.quantity(), productOrderInfo.requestQuantity());
        }

        // Verify other fields in the ProductOrderRequest
        assertNotNull(productOrderRequest.orderUUID());
        assertNotNull(productOrderRequest.invoiceUUID());
        assertNotNull(productOrderRequest.createdAt());

        // Verify kafka call
        verify(kafkaTemplate, times(1)).send(anyString(), any(ProductOrderRequest.class));
    }

    @Test
    @Order(2)
    public void testPlaceOrder_InsufficientQuantity_ThrowsInsufficientProductQuantityException() throws Exception {
        OrderRequest[] orderRequests = {
                new OrderRequest("productId1", 200), // Insufficient Quantity
                new OrderRequest("productId2", 5)
        };

        assertThrows(InsufficientProductQuantityException.class, () -> {
            productService.placeOrder(orderRequests);
        });
    }

    @Test
    @Order(3)
    public void testPlaceOrder_ProductNotFound_ThrowsProductNotFoundException() throws Exception {
        OrderRequest[] orderRequests = {
                new OrderRequest("nonExistentProductId", 2)
        };

        assertThrows(ProductNotFoundException.class, () -> {
            productService.placeOrder(orderRequests);
        });
    }

}
