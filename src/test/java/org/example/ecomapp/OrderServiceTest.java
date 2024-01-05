package org.example.ecomapp;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.example.ecomapp.models.Cart;
import org.example.ecomapp.models.CartItem;
import org.example.ecomapp.models.Order;
import org.example.ecomapp.models.Product;
import org.example.ecomapp.repositories.OrderRepository;
import org.example.ecomapp.services.CartService;
import org.example.ecomapp.services.OrderService;
import org.example.ecomapp.services.ProductService;
import org.example.ecomapp.services.StripeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

@SpringBootTest
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartService cartService;

    @InjectMocks
    private OrderService orderService;

    @Mock
    private ProductService productService;

    @Mock
    private StripeService stripeService;

    @BeforeEach
    void setUp() {
        Product prod1 = new Product("prod1", "Product 1", "Description", 20.0);
        Product prod2 = new Product("prod2", "Product 2", "Description", 30.0);

        when(productService.findById("prod1")).thenReturn(Mono.just(prod1));
        when(productService.findById("prod2")).thenReturn(Mono.just(prod2));
    }

    @Test
    void testPlaceOrder() throws StripeException {
        String userId = "user1";
        Product prod1 = new Product("prod1", "Product 1", "Description", 20.0);
        Product prod2 = new Product("prod2", "Product 2", "Description", 30.0);
        Cart cart = new Cart("user1");
        cart.addItem(new CartItem(prod1, 2));
        cart.addItem(new CartItem(prod2, 3));

        Order order = new Order("order1", userId, cart.getItems(), LocalDateTime.now(), 150.0, "payment1");

        when(cartService.findByUserId(userId)).thenReturn(Mono.just(cart));
        when(orderRepository.save(any(Order.class))).thenReturn(Mono.just(order));
        when(cartService.clearCart(userId)).thenReturn(Mono.empty());

        PaymentIntent mockPaymentIntent = Mockito.mock(PaymentIntent.class);
        when(mockPaymentIntent.getId()).thenReturn("mockPaymentIntentId");

        when(stripeService.createPaymentIntent(anyDouble(), anyString())).thenReturn(mockPaymentIntent);

        Mono<Order> result = orderService.placeOrder(userId);

        StepVerifier.create(result)
            .expectNextMatches(o ->
                o.getTotalAmount() == 130 &&
                    o.getUserId().equals(userId) &&
                    o.getItems().size() == 2)
            .verifyComplete();

        verify(cartService, times(1)).clearCart(userId);
    }

    @Test
    void testCalculateTotalAmount() {
        Product prod1 = new Product("prod1", "Product 1", "Description", 20.0);
        Product prod2 = new Product("prod2", "Product 2", "Description", 30.0);
        Cart cart = new Cart("user1");
        cart.addItem(new CartItem(prod1, 2));
        cart.addItem(new CartItem(prod2, 3));


        Mono<Double> totalAmount = orderService.calculateTotalAmount(cart);

        StepVerifier.create(totalAmount)
            .expectNext(130.0)
            .verifyComplete();
    }
}

