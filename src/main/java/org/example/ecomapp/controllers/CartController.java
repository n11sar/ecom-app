package org.example.ecomapp.controllers;

import org.example.ecomapp.models.Cart;
import org.example.ecomapp.models.CartItem;
import org.example.ecomapp.services.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/carts")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/{userId}/add")
    public Mono<ResponseEntity<Cart>> addItemToCart(@PathVariable String userId, @RequestBody CartItem item) {
        return cartService.addToCart(userId, item)
            .map(updatedCart -> ResponseEntity.ok().body(updatedCart));
    }

    @DeleteMapping("/{userId}/remove/{productId}")
    public Mono<ResponseEntity<Cart>> removeItemFromCart(@PathVariable String userId, @PathVariable String productId) {
        return cartService.removeFromCart(userId, productId)
            .map(updatedCart -> ResponseEntity.ok().body(updatedCart));
    }
    @GetMapping("/{userId}")
    public Mono<ResponseEntity<Cart>> getCart(@PathVariable String userId) {
        return cartService.findByUserId(userId)
            .map(cart -> ResponseEntity.ok().body(cart));
    }
}
