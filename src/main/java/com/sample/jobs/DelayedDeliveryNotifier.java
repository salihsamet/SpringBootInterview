package com.sample.jobs;

import com.sample.model.Delivery;
import com.sample.service.DeliveryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DelayedDeliveryNotifier {

    private static final Logger LOG = LoggerFactory.getLogger(DelayedDeliveryNotifier.class);

    @Autowired
    DeliveryService deliveryService;

    /**
     *  Use this method for the TASK 3
     */
    @Scheduled(fixedDelay = 30000)
    public void checkDelayedDeliveries() {
        List<Delivery> deliveries = deliveryService.findDeliveryDurationGreaterThanMinutes(45);
        if(deliveries.size() > 0) {
            List<String> emails = new ArrayList<String>();
            for (Delivery delivery:deliveries) {
                emails.add(delivery.getDeliveryMan().getEmail());
            }
            notifyCustomerSupport(emails);
        }
    }


    /**
     * This method should be called to notify customer support team
     * It just writes notification on console but it may be email or push notification in real.
     * So that this method should run in an async way.
     */
    @Async
    public void notifyCustomerSupport(List<String> emails) {
        for(String email: emails) {
            System.out.println(email);
        }
    }
}
