package com.davidfrivas.store;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

//@Service("stripe")
//@Primary // Stripe is the primary or default payment service
public class StripePaymentService implements PaymentService {
    @Value("${stripe.apiUrl}") // Inject Stripe API URL
    private String apiUrl;

    @Value("${stripe.enabled}")
    private boolean enabled;

    // Assign default value of 3000 in case this property is not defined in application.properties
    @Value("${stripe.timeout:3000}")
    private int timeout;

    @Value("${stripe.supported-currencies}")
    private List<String> supportedCurrencies;

    @Override
    public void processPayment(double amount) {
        System.out.println("STRIPE");
        System.out.println("API URL: " + apiUrl);
        System.out.println("Enabled: " + enabled);
        System.out.println("Timeout: " + timeout);
        System.out.println("Currencies: " + supportedCurrencies);
        System.out.println("Amount: " + amount);
    }
}
