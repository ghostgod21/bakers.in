package com.bakersin.repository;

import com.bakersin.model.Category;
import com.bakersin.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(Category category);
    List<Product> findByCategorySlug(String slug);
    List<Product> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);
    List<Product> findByOnSaleTrue();
    List<Product> findTop8ByOrderByRatingDesc();
}
