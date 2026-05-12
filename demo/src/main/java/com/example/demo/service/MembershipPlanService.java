package com.example.demo.service;

import com.example.demo.dto.MembershipPlanDto;
import com.example.demo.model.MembershipPlan;
import com.example.demo.model.enums.MembershipStatus;

import java.util.List;
import java.util.Optional;

public interface MembershipPlanService {
    MembershipPlan create(MembershipPlanDto dto);
    MembershipPlan update(MembershipPlanDto dto);
    Optional<MembershipPlan> findById(Long id);
    List<MembershipPlan> findAll();
    List<MembershipPlan> findByStatus(MembershipStatus status);
    void delete(Long id);
}
