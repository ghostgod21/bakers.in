package com.bakersin.controller;

import com.bakersin.model.Cart;
import com.bakersin.service.CartService;
import com.bakersin.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CartController {

    private final CartService cartService;
    private final ProductService productService;

    public CartController(CartService cartService, ProductService productService) {
        this.cartService = cartService;
        this.productService = productService;
    }

    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model model) {
        Cart cart = cartService.getOrCreateCart(session.getId());
        model.addAttribute("categories", productService.getAllCategories());
        model.addAttribute("cart", cart);
        model.addAttribute("deliveryFee", cartService.getDeliveryFee(cart));
        model.addAttribute("grandTotal", cartService.getGrandTotal(cart));
        model.addAttribute("freeDeliveryThreshold", CartService.FREE_DELIVERY_THRESHOLD);
        return "cart";
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long productId,
                             @RequestParam(defaultValue = "1") int quantity,
                             @RequestParam(required = false) String redirectTo,
                             HttpSession session) {
        cartService.addToCart(session.getId(), productId, quantity);
        return "redirect:" + (redirectTo != null && !redirectTo.isBlank() ? redirectTo : "/cart");
    }

    @PostMapping("/cart/update")
    public String updateQuantity(@RequestParam Long productId,
                                  @RequestParam int quantity,
                                  HttpSession session) {
        cartService.updateQuantity(session.getId(), productId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam Long productId, HttpSession session) {
        cartService.removeFromCart(session.getId(), productId);
        return "redirect:/cart";
    }
}
