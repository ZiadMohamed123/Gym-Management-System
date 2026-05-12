package com.example.demo.service;

import com.example.demo.dto.SubscriptionDto;
import com.example.demo.model.MembershipSubscription;

import java.util.List;
import java.util.Optional;

public interface SubscriptionService {
    MembershipSubscription assign(SubscriptionDto dto);
    MembershipSubscription renew(Long subscriptionId, SubscriptionDto dto);
    Optional<MembershipSubscription> findActiveByMemberId(Long memberId);
    List<MembershipSubscription> findByMemberId(Long memberId);
    void processExpiredSubscriptions();
    long countActiveSubscriptions();
}
