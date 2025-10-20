package com.davidfrivas.store;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // This class is a source of bean definitions
public class AppConfig {
    @Value("${payment-gateway:stripe}")
    private String paymentGateway;

    @Bean // This method is a bean producer
    public PaymentService stripe() {
        return new StripePaymentService();
    }

    @Bean
    public PaymentService paypal() {
        return new PayPalPaymentService();
    }

    @Bean
    public OrderService orderService() {
        if (paymentGateway.equals("stripe"))
            return new OrderService(stripe());

        return new OrderService(paypal());
    }
}
