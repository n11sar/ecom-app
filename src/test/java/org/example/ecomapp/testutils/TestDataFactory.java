package org.example.ecomapp.testutils;

import java.time.LocalDateTime;
import java.util.List;
import org.example.ecomapp.models.Cart;
import org.example.ecomapp.models.CartItem;
import org.example.ecomapp.models.Order;
import org.example.ecomapp.models.Product;

public class TestDataFactory {

    public static Cart createTestCart(String userId) {

        return new Cart("cart1", userId, createTestCartItems());
    }

    public static Order createTestOrder(String orderId, String userId, String paymentIntentId){
        return new Order(orderId, userId, createTestCartItems(), LocalDateTime.now(), calculateTotalAmount(createTestCartItems()), paymentIntentId);
    }

    private static List<CartItem> createTestCartItems() {
        return List.of(
            new CartItem(new Product("prod1", "Product 1", "Description 1", 50.0), 2),
            new CartItem(new Product("prod2", "Product 2", "Description 2", 30.0), 1),
            new CartItem(new Product("prod3", "Product 3", "Description 3", 20.0), 3)
        );
    }

    public static double calculateTotalAmount(List<CartItem> cartItems) {
        double totalAmount = 0;
        for (CartItem item : cartItems) {
            totalAmount += item.getProduct().getPrice() * item.getQuantity();
        }
        return totalAmount;
    }

}

