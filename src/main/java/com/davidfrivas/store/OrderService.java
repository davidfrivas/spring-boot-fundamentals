package com.davidfrivas.store;

public class OrderService {
    // Private field to store payment service
    private PaymentService paymentService;

    // Constructor injection
    // Pass our dependency as an argument to this constructor
    public OrderService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public void placeOrder() {
        paymentService.processPayment(10);
    }

    // Setter injection: setter method for dependency injection
    public void setPaymentService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
