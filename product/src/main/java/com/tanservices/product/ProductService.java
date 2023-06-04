package com.tanservices.product;

import com.tanservices.product.exception.InsufficientProductQuantityException;
import com.tanservices.product.exception.ProductNotFoundException;
import com.tanservices.product.producer.ProductOrderInfo;
import com.tanservices.product.producer.ProductOrderRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    @Value("${kafka-topic-name}")
    private String topic;
    private KafkaTemplate<String, ProductOrderRequest> kafkaTemplate;
    private final ProductRepository productRepository;

    public ProductService(KafkaTemplate<String, ProductOrderRequest> kafkaTemplate, ProductRepository productRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.productRepository = productRepository;
    }

    public ProductOrderRequest placeOrder(OrderRequest[] orderRequests) {
        this.checkRequestedProducts(orderRequests);
        List<ProductOrderInfo> productOrderInfos = new ArrayList<>();


        for (OrderRequest orderRequest : orderRequests) {
            String productId = orderRequest.productId();
            int requestQuantity = orderRequest.quantity();

            Product product = productRepository.findById(productId).get();
            product.setQuantity(product.getQuantity() - requestQuantity);
            productRepository.save(product);

            productOrderInfos.add(new ProductOrderInfo(product, requestQuantity));
        }

        ProductOrderRequest productOrderRequest = new ProductOrderRequest(productOrderInfos);

        System.out.println(productOrderRequest);

        kafkaTemplate.send(topic, productOrderRequest);

        return productOrderRequest;
    }

    private void checkRequestedProducts(OrderRequest[] orderRequests) {
        for (OrderRequest orderRequest : orderRequests) {
            String productId = orderRequest.productId();
            int requestQuantity = orderRequest.quantity();

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException(productId));

            if (product.getQuantity() < requestQuantity) {
                throw new InsufficientProductQuantityException(productId);
            }
        }
    }
}
