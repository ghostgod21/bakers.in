package com.bakersin.controller;

import com.bakersin.model.Product;
import com.bakersin.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public String allProducts(@RequestParam(required = false) String q, Model model) {
        model.addAttribute("categories", productService.getAllCategories());
        if (q != null && !q.isBlank()) {
            model.addAttribute("products", productService.searchProducts(q));
            model.addAttribute("pageTitle", "Search results for \"" + q + "\"");
            model.addAttribute("query", q);
        } else {
            model.addAttribute("products", productService.getAllProducts());
            model.addAttribute("pageTitle", "Our Full Menu");
        }
        return "products";
    }

    @GetMapping("/category/{slug}")
    public String byCategory(@PathVariable String slug, Model model) {
        var category = productService.getCategoryBySlug(slug).orElse(null);
        model.addAttribute("categories", productService.getAllCategories());
        model.addAttribute("currentCategory", category);
        model.addAttribute("products", productService.getProductsByCategorySlug(slug));
        model.addAttribute("pageTitle", category != null ? category.getName() : "Category");
        return "products";
    }

    @GetMapping("/product/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));
        model.addAttribute("categories", productService.getAllCategories());
        model.addAttribute("product", product);
        model.addAttribute("related", productService.getRelatedProducts(product));
        return "product-detail";
    }
}
