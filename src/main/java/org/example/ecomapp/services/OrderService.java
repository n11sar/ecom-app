package org.example.ecomapp.services;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import java.time.LocalDateTime;
import java.util.UUID;
import org.example.ecomapp.models.Cart;
import org.example.ecomapp.models.Order;
import org.example.ecomapp.repositories.OrderRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final StripeService stripeService;


    public OrderService(OrderRepository orderRepository, CartService cartService,
        StripeService stripeService) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.stripeService = stripeService;
    }

public Mono<Order> placeOrder(String userId) {
    return cartService.findByUserId(userId)
        .flatMap(cart -> calculateTotalAmount(cart)
            .flatMap(totalAmount -> {
                try {
                    PaymentIntent paymentIntent = stripeService.createPaymentIntent(totalAmount, "usd");
                    Order order = createOrder(userId, cart, totalAmount, paymentIntent.getId());
                    return orderRepository.save(order)
                        .then(cartService.clearCart(userId))
                        .thenReturn(order);
                } catch (StripeException e) {
                    return Mono.error(e); // Handle Stripe exceptions appropriately
                }
            }));
}

    private Order createOrder(String userId, Cart cart, double totalAmount, String paymentIntent) {
        String uniqueID = UUID.randomUUID().toString();
        return new Order(uniqueID, userId, cart.getItems(), LocalDateTime.now(), totalAmount, paymentIntent);
    }

    public Mono<Double> calculateTotalAmount(Cart cart) {
        return Flux.fromIterable(cart.getItems())
            .flatMap(item -> Mono.justOrEmpty(item.getProduct().getPrice() * item.getQuantity()))
            .reduce(0.0, Double::sum);
    }


    public Mono<Order> findById(String id) {
        return orderRepository.findById(id);
    }
}
