package org.example.ecomapp.models;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    @Id
    private String id;
    private String userId;
    private List<CartItem> items;

    public Cart(String userId) {
        this.userId = userId;
        this.items = new ArrayList<>(); // Инициализация списка
    }

    public void addItem(CartItem item) {
        this.items.stream()
            .filter(cartItem -> cartItem.getProduct().getId().equals(item.getProduct().getId()))
            .findAny()
            .ifPresentOrElse(
                existingItem -> existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity()),
                () -> this.items.add(item)
            );
    }

    public void removeItem(String productId) {
        this.items.removeIf(item -> item.getProduct().getId().equals(productId));
    }
}
