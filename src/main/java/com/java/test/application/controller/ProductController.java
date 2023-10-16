package com.java.test.application.controller;

import com.java.test.application.model.Product;
import com.java.test.application.service.impl.ProductServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RequestMapping("/api/products")
@AllArgsConstructor
@RestController
public class ProductController {
    private final ProductServiceImpl productService;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Product> addGoods(@RequestBody final Product product) {
        Product createdProduct = productService.addProduct(product);
        URI uri = productService.generateProductURI(createdProduct.getId());
        return productService.createProductResponse(uri, createdProduct);
    }

    @GetMapping("")
    public ResponseEntity<List<Product>> getProductList() {
        return ResponseEntity.ok(productService.getProductList());
    }

}
