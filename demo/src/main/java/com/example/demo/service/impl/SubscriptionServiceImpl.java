package com.example.demo.service.impl;

import com.example.demo.dto.SubscriptionDto;
import com.example.demo.model.Member;
import com.example.demo.model.MembershipPlan;
import com.example.demo.model.MembershipSubscription;
import com.example.demo.model.enums.AccountStatus;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.MembershipPlanRepository;
import com.example.demo.repository.MembershipSubscriptionRepository;
import com.example.demo.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SubscriptionServiceImpl implements SubscriptionService {

    private final MembershipSubscriptionRepository subscriptionRepository;
    private final MemberRepository memberRepository;
    private final MembershipPlanRepository planRepository;

    @Override
    public MembershipSubscription assign(SubscriptionDto dto) {
        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        MembershipPlan plan = planRepository.findById(dto.getPlanId())
                .orElseThrow(() -> new IllegalArgumentException("Plan not found"));

        // Deactivate existing active subscription
        subscriptionRepository.findByMemberAndActiveTrue(member).ifPresent(existing -> {
            existing.setActive(false);
            subscriptionRepository.save(existing);
        });

        LocalDate startDate = dto.getStartDate();
        LocalDate endDate = startDate.plusMonths(plan.getDuration().getMonths());

        MembershipSubscription subscription = MembershipSubscription.builder()
                .member(member)
                .plan(plan)
                .startDate(startDate)
                .endDate(endDate)
                .paymentStatus(dto.getPaymentStatus())
                .active(true)
                .build();

        // Activate member if suspended/inactive
        if (member.getAccountStatus() != AccountStatus.ACTIVE) {
            member.setAccountStatus(AccountStatus.ACTIVE);
            memberRepository.save(member);
        }

        return subscriptionRepository.save(subscription);
    }

    @Override
    public MembershipSubscription renew(Long subscriptionId, SubscriptionDto dto) {
        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        MembershipPlan plan = planRepository.findById(dto.getPlanId())
                .orElseThrow(() -> new IllegalArgumentException("Plan not found"));

        // Deactivate current
        subscriptionRepository.findByMemberAndActiveTrue(member).ifPresent(existing -> {
            existing.setActive(false);
            subscriptionRepository.save(existing);
        });

        LocalDate startDate = dto.getStartDate();
        LocalDate endDate = startDate.plusMonths(plan.getDuration().getMonths());

        MembershipSubscription renewed = MembershipSubscription.builder()
                .member(member)
                .plan(plan)
                .startDate(startDate)
                .endDate(endDate)
                .paymentStatus(dto.getPaymentStatus())
                .active(true)
                .build();

        return subscriptionRepository.save(renewed);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MembershipSubscription> findActiveByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId).orElse(null);
        if (member == null) return Optional.empty();
        return subscriptionRepository.findByMemberAndActiveTrue(member);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MembershipSubscription> findByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        return subscriptionRepository.findByMember(member);
    }

    @Override
    public void processExpiredSubscriptions() {
        List<MembershipSubscription> expired = subscriptionRepository.findExpiredSubscriptions(LocalDate.now());
        for (MembershipSubscription sub : expired) {
            sub.setActive(false);
            subscriptionRepository.save(sub);

            // Mark member as inactive if no other active subscription
            Member member = sub.getMember();
            boolean hasActive = subscriptionRepository.findByMemberAndActiveTrue(member).isPresent();
            if (!hasActive && member.getAccountStatus() == AccountStatus.ACTIVE) {
                member.setAccountStatus(AccountStatus.INACTIVE);
                memberRepository.save(member);
                log.info("Member {} marked as INACTIVE due to expired subscription", member.getMemberId());
            }
        }
        log.info("Processed {} expired subscriptions", expired.size());
    }

    @Override
    @Transactional(readOnly = true)
    public long countActiveSubscriptions() {
        return subscriptionRepository.countByActiveTrue();
    }
}
