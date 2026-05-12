package com.example.demo.service.impl;

import com.example.demo.dto.MemberRegistrationDto;
import com.example.demo.dto.MemberUpdateDto;
import com.example.demo.model.Member;
import com.example.demo.model.MemberStatusHistory;
import com.example.demo.model.enums.AccountStatus;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.MemberStatusHistoryRepository;
import com.example.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberStatusHistoryRepository statusHistoryRepository;

    @Override
    public Member register(MemberRegistrationDto dto) {
        if (memberRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("A member with this email already exists.");
        }
        if (memberRepository.existsByPhoneNumber(dto.getPhoneNumber())) {
            throw new IllegalArgumentException("A member with this phone number already exists.");
        }

        Member member = Member.builder()
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .dateOfBirth(dto.getDateOfBirth())
                .address(dto.getAddress())
                .accountStatus(AccountStatus.ACTIVE)
                .registrationDate(LocalDate.now())
                .build();

        member = memberRepository.save(member);

        // Generate unique member ID: GYM-00001
        String memberId = "GYM-" + String.format("%05d", member.getId());
        member.setMemberId(memberId);
        member = memberRepository.save(member);

        // Record initial status history
        MemberStatusHistory history = MemberStatusHistory.builder()
                .member(member)
                .oldStatus(null)
                .newStatus(AccountStatus.ACTIVE)
                .changedAt(LocalDateTime.now())
                .reason("Initial registration")
                .build();
        statusHistoryRepository.save(history);

        return member;
    }

    @Override
    public Member update(MemberUpdateDto dto) {
        Member member = memberRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + dto.getId()));

        // Check email uniqueness (excluding current member)
        memberRepository.findByEmail(dto.getEmail()).ifPresent(existing -> {
            if (!existing.getId().equals(dto.getId())) {
                throw new IllegalArgumentException("Email is already used by another member.");
            }
        });

        AccountStatus oldStatus = member.getAccountStatus();

        member.setFullName(dto.getFullName());
        member.setEmail(dto.getEmail());
        member.setPhoneNumber(dto.getPhoneNumber());
        member.setDateOfBirth(dto.getDateOfBirth());
        member.setAddress(dto.getAddress());
        member.setAccountStatus(dto.getAccountStatus());

        member = memberRepository.save(member);

        // If status changed, record history
        if (!oldStatus.equals(dto.getAccountStatus())) {
            MemberStatusHistory history = MemberStatusHistory.builder()
                    .member(member)
                    .oldStatus(oldStatus)
                    .newStatus(dto.getAccountStatus())
                    .changedAt(LocalDateTime.now())
                    .reason(dto.getStatusChangeReason())
                    .build();
            statusHistoryRepository.save(history);
        }

        return member;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Member> findByMemberId(String memberId) {
        return memberRepository.findByMemberId(memberId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Member> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return memberRepository.findAll();
        }
        return memberRepository.searchByKeyword(keyword.trim());
    }

    @Override
    public void delete(Long id) {
        memberRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long countTotal() {
        return memberRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long countActive() {
        return memberRepository.countByAccountStatus(AccountStatus.ACTIVE);
    }
}
