package com.tanservices.product;

import com.tanservices.product.exception.InsufficientProductQuantityException;
import com.tanservices.product.exception.ProductNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
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

        // TODO: send productOrderRequest to kafka topic

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
