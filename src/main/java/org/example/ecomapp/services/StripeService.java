package org.example.ecomapp.services;


import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.stereotype.Service;

@Service
public class StripeService {




    public PaymentIntent createPaymentIntent(double totalAmount, String currency) throws StripeException {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
            .setAmount((long) (totalAmount * 100))
            .setCurrency(currency)
            .build();

        return PaymentIntent.create(params);
    }
}
