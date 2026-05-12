package com.example.demo.service.impl;

import com.example.demo.dto.MembershipPlanDto;
import com.example.demo.model.MembershipPlan;
import com.example.demo.model.enums.MembershipStatus;
import com.example.demo.repository.MembershipPlanRepository;
import com.example.demo.service.MembershipPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MembershipPlanServiceImpl implements MembershipPlanService {

    private final MembershipPlanRepository planRepository;

    @Override
    public MembershipPlan create(MembershipPlanDto dto) {
        MembershipPlan plan = MembershipPlan.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .duration(dto.getDuration())
                .price(dto.getPrice())
                .status(dto.getStatus())
                .build();
        return planRepository.save(plan);
    }

    @Override
    public MembershipPlan update(MembershipPlanDto dto) {
        MembershipPlan plan = planRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Plan not found"));
        plan.setName(dto.getName());
        plan.setDescription(dto.getDescription());
        plan.setDuration(dto.getDuration());
        plan.setPrice(dto.getPrice());
        plan.setStatus(dto.getStatus());
        return planRepository.save(plan);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MembershipPlan> findById(Long id) {
        return planRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MembershipPlan> findAll() {
        return planRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MembershipPlan> findByStatus(MembershipStatus status) {
        return planRepository.findByStatus(status);
    }

    @Override
    public void delete(Long id) {
        planRepository.deleteById(id);
    }
}
