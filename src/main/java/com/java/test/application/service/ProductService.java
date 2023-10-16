package com.java.test.application.service;

import com.java.test.application.model.Product;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.List;

public interface ProductService {
    Product addProduct(Product product);

    public List<Product> getProductList();

    URI generateProductURI(Long productId);

    ResponseEntity<Product> createProductResponse(URI uri, Product product);
}
