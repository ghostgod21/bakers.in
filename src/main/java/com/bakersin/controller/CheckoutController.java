package com.bakersin.controller;

import com.bakersin.model.Cart;
import com.bakersin.model.Order;
import com.bakersin.service.CartService;
import com.bakersin.service.OrderService;
import com.bakersin.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CheckoutController {

    private final CartService cartService;
    private final OrderService orderService;
    private final ProductService productService;

    public CheckoutController(CartService cartService, OrderService orderService, ProductService productService) {
        this.cartService = cartService;
        this.orderService = orderService;
        this.productService = productService;
    }

    @GetMapping("/checkout")
    public String checkoutForm(HttpSession session, Model model) {
        Cart cart = cartService.getOrCreateCart(session.getId());
        if (cart.getItems().isEmpty()) {
            return "redirect:/cart";
        }
        model.addAttribute("categories", productService.getAllCategories());
        model.addAttribute("cart", cart);
        model.addAttribute("deliveryFee", cartService.getDeliveryFee(cart));
        model.addAttribute("grandTotal", cartService.getGrandTotal(cart));
        return "checkout";
    }

    @PostMapping("/checkout")
    public String placeOrder(@RequestParam String customerName,
                              @RequestParam String email,
                              @RequestParam String phone,
                              @RequestParam String address,
                              @RequestParam String city,
                              @RequestParam String zip,
                              @RequestParam String paymentMethod,
                              HttpSession session,
                              Model model) {
        try {
            Order order = orderService.placeOrder(session.getId(), customerName, email, phone,
                    address, city, zip, paymentMethod);
            return "redirect:/order-confirmation/" + order.getOrderNumber();
        } catch (IllegalStateException ex) {
            return "redirect:/cart";
        }
    }

    @GetMapping("/order-confirmation/{orderNumber}")
    public String confirmation(@PathVariable String orderNumber, Model model) {
        Order order = orderService.getByOrderNumber(orderNumber)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderNumber));
        model.addAttribute("categories", productService.getAllCategories());
        model.addAttribute("order", order);
        return "order-confirmation";
    }
}
