package com.davidfrivas.store;

public class OrderService {
    // Private field to store payment service
    private PaymentService paymentService;

    // Pass our dependency as an argument to this constructor
    public OrderService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public void placeOrder() {
        paymentService.processPayment(10);
    }
}
