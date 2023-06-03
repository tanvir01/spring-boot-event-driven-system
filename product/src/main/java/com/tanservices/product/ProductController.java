package com.tanservices.product;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/order-request")
    public ResponseEntity<Map<String, String>> orderProduct(@RequestBody OrderRequest[] orderRequests) {
        ProductOrderRequest productOrderRequest = productService.placeOrder(orderRequests);

        Map<String, String> response = new HashMap<>();
        response.put("orderUUID", String.valueOf(productOrderRequest.orderUUID()));
        response.put("invoiceUUID", String.valueOf(productOrderRequest.invoiceUUID()));

        return ResponseEntity.ok(response);
    }
}
