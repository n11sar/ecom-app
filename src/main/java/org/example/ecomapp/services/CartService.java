package org.example.ecomapp.services;

import java.util.Collections;
import org.example.ecomapp.models.Cart;
import org.example.ecomapp.models.CartItem;
import org.example.ecomapp.repositories.CartRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CartService {
    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public Mono<Cart> addToCart(String userId, CartItem item) {
        return cartRepository.findByUserId(userId)
            .defaultIfEmpty(new Cart(userId))
            .flatMap(cart -> {
                cart.addItem(item);
                return cartRepository.save(cart);
            });
    }

    public Mono<Cart> removeFromCart(String userId, String productId) {
        return cartRepository.findByUserId(userId)
            .flatMap(cart -> {
                cart.removeItem(productId);
                return cartRepository.save(cart);
            });
    }

    public Mono<Cart> findByUserId(String userId) {
       return cartRepository.findByUserId(userId);
    }

    public Mono<Void> clearCart(String userId) {
        return cartRepository.findByUserId(userId)
            .flatMap(cart -> {
                cart.setItems(Collections.emptyList());
                return cartRepository.save(cart);
            }).then();
    }
}
