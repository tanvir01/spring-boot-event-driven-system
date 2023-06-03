package com.tanservices.product;

import com.tanservices.product.exception.InsufficientProductQuantityException;
import com.tanservices.product.exception.ProductNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void placeOrder(ProductOrderRequest productOrderRequest) {
        String productId = productOrderRequest.productId();
        int quantity = productOrderRequest.quantity();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        if(product.getQuantity() < quantity) {
            throw new InsufficientProductQuantityException(productId);
        }

        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);

        // TODO: more work to be done
    }
}
