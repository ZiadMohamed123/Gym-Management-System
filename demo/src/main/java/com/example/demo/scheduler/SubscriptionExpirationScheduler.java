package com.example.demo.scheduler;

import com.example.demo.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SubscriptionExpirationScheduler {

    private final SubscriptionService subscriptionService;

    /**
     * Runs every day at midnight to expire subscriptions
     * Cron: second minute hour dayOfMonth month dayOfWeek
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void checkAndExpireSubscriptions() {
        log.info("Running subscription expiration check...");
        subscriptionService.processExpiredSubscriptions();
    }
}
