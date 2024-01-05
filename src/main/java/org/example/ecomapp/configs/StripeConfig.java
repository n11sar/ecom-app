package org.example.ecomapp.configs;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfig {


    @Value("${STRIPE_API_KEY}")
    private String apiKey;

    @SuppressWarnings("java:S2696")
    @PostConstruct
    public void init() {
        Stripe.apiKey = apiKey;
    }

}
