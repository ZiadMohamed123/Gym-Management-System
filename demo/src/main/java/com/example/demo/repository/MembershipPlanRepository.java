package com.example.demo.repository;

import com.example.demo.model.MembershipPlan;
import com.example.demo.model.enums.MembershipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MembershipPlanRepository extends JpaRepository<MembershipPlan, Long> {

    List<MembershipPlan> findByStatus(MembershipStatus status);
}
