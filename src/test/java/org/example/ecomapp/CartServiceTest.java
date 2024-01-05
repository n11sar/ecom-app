package org.example.ecomapp;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.example.ecomapp.models.Cart;
import org.example.ecomapp.models.CartItem;
import org.example.ecomapp.models.Product;
import org.example.ecomapp.repositories.CartRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureWebTestClient
class CartServiceTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CartRepository cartRepository;

    @Test
    void addToCartTest() {
        String userId = "user123";
        CartItem newItem = new CartItem(new Product("prod1", "Product 1", "Description 1", 50.0), 1);
        Cart existingCart = new Cart("cart1", userId, new ArrayList<>());

        when(cartRepository.findByUserId(userId)).thenReturn(Mono.just(existingCart));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        webTestClient.post().uri("/carts/{userId}/add", userId)
            .bodyValue(newItem)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.userId").isEqualTo(userId)
            .jsonPath("$.items[0].product.id").isEqualTo("prod1");

        verify(cartRepository, times(1)).findByUserId(userId);
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void removeFromCartTest() {
        String userId = "user123";
        String productId = "prod1";
        Cart existingCart = new Cart("cart1", userId, new ArrayList<>(
            List.of(new CartItem(new Product(productId, "Product 1", "Description 1", 50.0), 1))));

        when(cartRepository.findByUserId(userId)).thenReturn(Mono.just(existingCart));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        webTestClient.delete().uri("/carts/{userId}/remove/{productId}", userId, productId)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.userId").isEqualTo(userId)
            .jsonPath("$.items.length()").isEqualTo(0);

        verify(cartRepository, times(1)).findByUserId(userId);
        verify(cartRepository, times(1)).save(any(Cart.class));
    }




}


