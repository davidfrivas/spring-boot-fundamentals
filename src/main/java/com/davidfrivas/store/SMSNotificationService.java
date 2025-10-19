package com.davidfrivas.store;

import org.springframework.stereotype.Service;

@Service("sms-notification")
public class SMSNotificationService implements NotificationService {
    @Override
    public void send(String message) {
        System.out.println("Sending SMS: " + message);
    }
}
