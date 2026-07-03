package com.bakersin.service;

import com.bakersin.model.Cart;
import com.bakersin.model.CartItem;
import com.bakersin.model.Product;
import com.bakersin.repository.CartRepository;
import com.bakersin.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class CartService {

    /** Orders become free-delivery above this subtotal (INR). */
    public static final BigDecimal FREE_DELIVERY_THRESHOLD = BigDecimal.valueOf(499);
    public static final BigDecimal DELIVERY_FEE = BigDecimal.valueOf(49);

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Cart getOrCreateCart(String sessionId) {
        return cartRepository.findBySessionId(sessionId)
                .orElseGet(() -> cartRepository.save(new Cart(sessionId)));
    }

    @Transactional
    public Cart addToCart(String sessionId, Long productId, int quantity) {
        Cart cart = getOrCreateCart(sessionId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

        cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresentOrElse(
                        item -> item.setQuantity(item.getQuantity() + quantity),
                        () -> cart.getItems().add(new CartItem(cart, product, quantity))
                );

        return cartRepository.save(cart);
    }

    @Transactional
    public Cart updateQuantity(String sessionId, Long productId, int quantity) {
        Cart cart = getOrCreateCart(sessionId);
        if (quantity <= 0) {
            cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
        } else {
            cart.getItems().stream()
                    .filter(item -> item.getProduct().getId().equals(productId))
                    .findFirst()
                    .ifPresent(item -> item.setQuantity(quantity));
        }
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart removeFromCart(String sessionId, Long productId) {
        Cart cart = getOrCreateCart(sessionId);
        cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
        return cartRepository.save(cart);
    }

    @Transactional
    public void clearCart(String sessionId) {
        Cart cart = getOrCreateCart(sessionId);
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    public BigDecimal getDeliveryFee(Cart cart) {
        if (cart.getSubtotal().compareTo(FREE_DELIVERY_THRESHOLD) >= 0 || cart.getItems().isEmpty()) {
            return BigDecimal.ZERO;
        }
        return DELIVERY_FEE;
    }

    public BigDecimal getGrandTotal(Cart cart) {
        return cart.getSubtotal().add(getDeliveryFee(cart));
    }
}
