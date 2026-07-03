package com.bakersin.controller;

import com.bakersin.model.Category;
import com.bakersin.service.CartService;
import com.bakersin.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

/**
 * Makes data needed by the header/footer on EVERY page (cart badge count and
 * the category list for nav) available without every controller having to
 * add it manually.
 */
@ControllerAdvice
public class GlobalModelAdvice {

    private final CartService cartService;
    private final ProductService productService;

    public GlobalModelAdvice(CartService cartService, ProductService productService) {
        this.cartService = cartService;
        this.productService = productService;
    }

    @ModelAttribute("cartCount")
    public int cartCount(HttpSession session) {
        return cartService.getOrCreateCart(session.getId()).getTotalItemCount();
    }

    @ModelAttribute("categories")
    public List<Category> categories() {
        return productService.getAllCategories();
    }
}
