package org.example.ecomapp;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import org.example.ecomapp.controllers.CartController;
import org.example.ecomapp.models.Cart;
import org.example.ecomapp.models.CartItem;
import org.example.ecomapp.models.Product;
import org.example.ecomapp.services.CartService;
import org.example.ecomapp.testutils.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@WebFluxTest(controllers = CartController.class)
 class CartControllerTest {

    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private CartService cartService;

    @Test
    void testGetCartByUserId() {
        String userId = "user123";

        Cart mockCart = TestDataFactory.createTestCart(userId);

        when(cartService.findByUserId(userId)).thenReturn(Mono.just(mockCart));

        webTestClient.get().uri("/carts/{userId}", userId)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.userId").isEqualTo(userId);

        verify(cartService, times(1)).findByUserId(userId);
    }

    @Test
    void addItemToCartTest() {
        String userId = "user123";
        CartItem newItem = new CartItem(new Product("prod1", "Product 1", "Description 1", 50.0), 1);
        Cart existingCart = new Cart("cart1", userId, new ArrayList<>());

        when(cartService.addToCart(userId, newItem)).thenReturn(Mono.just(existingCart));

        webTestClient.post().uri("/carts/{userId}/add", userId)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(newItem)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.userId").isEqualTo(userId);

        verify(cartService, times(1)).addToCart(userId, newItem);
    }

    @Test
    void removeItemFromCartTest() {
        String userId = "user123";
        String productId = "prod1";
        Cart updatedCart = new Cart("cart1", userId, new ArrayList<>());

        when(cartService.removeFromCart(userId, productId)).thenReturn(Mono.just(updatedCart));

        webTestClient.delete().uri("/carts/{userId}/remove/{productId}", userId, productId)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.userId").isEqualTo(userId);

        verify(cartService, times(1)).removeFromCart(userId, productId);
    }

}
