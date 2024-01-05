package org.example.ecomapp.repositories;

import org.example.ecomapp.models.Order;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface OrderRepository extends ReactiveMongoRepository<Order, String> {

}
