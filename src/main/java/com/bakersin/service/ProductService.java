package com.bakersin.service;

import com.bakersin.model.Category;
import com.bakersin.model.Product;
import com.bakersin.repository.CategoryRepository;
import com.bakersin.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryBySlug(String slug) {
        return categoryRepository.findBySlug(slug);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getProductsByCategorySlug(String slug) {
        return productRepository.findByCategorySlug(slug);
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public List<Product> searchProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword);
    }

    public List<Product> getFeaturedProducts() {
        return productRepository.findTop8ByOrderByRatingDesc();
    }

    public List<Product> getOnSaleProducts() {
        return productRepository.findByOnSaleTrue();
    }

    public List<Product> getRelatedProducts(Product product) {
        return productRepository.findByCategory(product.getCategory()).stream()
                .filter(p -> !p.getId().equals(product.getId()))
                .limit(4)
                .toList();
    }
}
