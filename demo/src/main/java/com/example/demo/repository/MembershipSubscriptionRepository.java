package com.example.demo.repository;

import com.example.demo.model.Member;
import com.example.demo.model.MembershipSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MembershipSubscriptionRepository extends JpaRepository<MembershipSubscription, Long> {

    List<MembershipSubscription> findByMember(Member member);

    Optional<MembershipSubscription> findByMemberAndActiveTrue(Member member);

    // Find subscriptions that have expired (endDate before today and still marked active)
    @Query("SELECT s FROM MembershipSubscription s WHERE s.endDate < :today AND s.active = true")
    List<MembershipSubscription> findExpiredSubscriptions(@Param("today") LocalDate today);

    long countByActiveTrue();
}
