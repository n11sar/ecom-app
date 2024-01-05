package org.example.ecomapp.controllers;

import org.example.ecomapp.models.Order;
import org.example.ecomapp.services.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Order>> getOrderById(@PathVariable String id){
        return orderService.findById(id)
            .map(order -> ResponseEntity.ok().body(order));
    }

    @PostMapping("/place/{userId}")
    public Mono<ResponseEntity<Order>> placeOrder(@PathVariable String userId) {
        return orderService.placeOrder(userId)
            .map(order -> ResponseEntity.ok().body(order));
    }
}
