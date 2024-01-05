package org.example.ecomapp.services;

import org.example.ecomapp.models.Product;
import org.example.ecomapp.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);


    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Flux<Product> findAll() {
        return productRepository.findAll();
    }

    public Mono<Product> findById(String id) {
        return productRepository.findById(id)
            .doOnNext(product -> log.info("Fetched product: {}", product))
            .doOnError(error -> log.error("Error fetching product with ID {}: {}", id, error.getMessage()));
    }
}
