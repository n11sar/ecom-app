package org.example.ecomapp;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.example.ecomapp.controllers.OrderController;
import org.example.ecomapp.models.Order;
import org.example.ecomapp.services.OrderService;
import org.example.ecomapp.testutils.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@WebFluxTest(controllers = OrderController.class)
class OrderControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private OrderService orderService;

    @Test
    void testGetOrderById() {
        String orderId = "order1";
        String userId = "user122";
        String paymentIntentId = "5";
        Order mockOrder = TestDataFactory.createTestOrder(orderId, userId, paymentIntentId);

        when(orderService.findById(orderId)).thenReturn(Mono.just(mockOrder));

        webTestClient.get().uri("/orders/{id}", orderId)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.id").isEqualTo(orderId);

        verify(orderService, times(1)).findById(orderId);
    }
}
