package com.bakersin.service;

import com.bakersin.model.Cart;
import com.bakersin.model.CartItem;
import com.bakersin.model.Order;
import com.bakersin.model.OrderItem;
import com.bakersin.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Optional;

@Service
public class OrderService {

    private static final String ALPHANUMERIC = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    private final OrderRepository orderRepository;
    private final CartService cartService;

    public OrderService(OrderRepository orderRepository, CartService cartService) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
    }

    @Transactional
    public Order placeOrder(String sessionId, String customerName, String email, String phone,
                             String address, String city, String zip, String paymentMethod) {
        Cart cart = cartService.getOrCreateCart(sessionId);
        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cannot place an order with an empty cart");
        }

        Order order = new Order();
        order.setOrderNumber(generateOrderNumber());
        order.setCustomerName(customerName);
        order.setEmail(email);
        order.setPhone(phone);
        order.setAddress(address);
        order.setCity(city);
        order.setZip(zip);
        order.setPaymentMethod(paymentMethod);
        order.setTotal(cartService.getGrandTotal(cart));

        for (CartItem item : cart.getItems()) {
            OrderItem orderItem = new OrderItem(
                    order,
                    item.getProduct().getName(),
                    item.getProduct().getFinalPrice(),
                    item.getQuantity()
            );
            order.getItems().add(orderItem);
        }

        Order saved = orderRepository.save(order);
        cartService.clearCart(sessionId);
        return saved;
    }

    public Optional<Order> getByOrderNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber);
    }

    private String generateOrderNumber() {
        StringBuilder sb = new StringBuilder("BAK-");
        for (int i = 0; i < 8; i++) {
            sb.append(ALPHANUMERIC.charAt(RANDOM.nextInt(ALPHANUMERIC.length())));
        }
        return sb.toString();
    }
}
