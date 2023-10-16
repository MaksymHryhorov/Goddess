package com.java.test.application.repository;

import com.java.test.application.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT SUM(p.price) FROM Product p WHERE p.id IN :productIds")
    Double calculateTotalPrice(@Param("productIds") List<Long> productIds);
}
