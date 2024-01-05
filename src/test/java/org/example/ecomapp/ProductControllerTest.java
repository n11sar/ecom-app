package org.example.ecomapp;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.example.ecomapp.controllers.ProductController;
import org.example.ecomapp.models.Product;
import org.example.ecomapp.services.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(controllers = ProductController.class)
class ProductControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ProductService productService;

    @Test
    void testGetAllProducts() {
        when(productService.findAll())
            .thenReturn(Flux.just(new Product("1", "Test Product", "Description", 100.0)));

        webTestClient.get().uri("/products")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(Product.class).hasSize(1);

        verify(productService, times(1)).findAll();
    }
    @Test
    void testGetProductById(){
        String productId =  "1";
        Product mockProduct = new Product(productId, "Test Product", "Description", 100.0);

        when(productService.findById(productId)).thenReturn(Mono.just(mockProduct));

        webTestClient.get().uri("/products/{id}", productId)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.id").isEqualTo(productId)
            .jsonPath("$.name").isEqualTo("Test Product");

        verify(productService, times(1)).findById(productId);
    }
}
