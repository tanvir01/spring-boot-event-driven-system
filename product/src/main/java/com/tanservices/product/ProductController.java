package com.tanservices.product;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/order-request")
    public ResponseEntity<Void> orderProduct(@RequestBody ProductOrderRequest productOrderRequest) {
        productService.placeOrder(productOrderRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
