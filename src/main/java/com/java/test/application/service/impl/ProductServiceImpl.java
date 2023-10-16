package com.java.test.application.service.impl;

import com.java.test.application.model.Product;
import com.java.test.application.repository.ProductRepository;
import com.java.test.application.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@AllArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getProductList() {
        return productRepository.findAll();
    }

    public URI generateProductURI(final Long productId) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productId)
                .toUri();
    }

    public ResponseEntity<Product> createProductResponse(final URI uri, final Product product) {
        return ResponseEntity.created(uri)
                .body(product);
    }
}
