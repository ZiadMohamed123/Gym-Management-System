package com.example.demo.service.impl;

import com.example.demo.dto.MemberRegistrationDto;
import com.example.demo.dto.MemberSignupDto;
import com.example.demo.dto.MemberUpdateDto;
import com.example.demo.model.AppUser;
import com.example.demo.model.Member;
import com.example.demo.model.MemberStatusHistory;
import com.example.demo.model.enums.AccountStatus;
import com.example.demo.model.enums.UserRole;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.MemberStatusHistoryRepository;
import com.example.demo.repository.AppUserRepository;
import com.example.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Member register(MemberRegistrationDto dto) {
        ensureUniqueMember(dto.getEmail(), dto.getPhoneNumber());
        Member member = createMember(dto.getFullName(), dto.getEmail(), dto.getPhoneNumber(),
                dto.getDateOfBirth(), dto.getAddress());
        createMemberAccount(member, "member123");
        return member;
    }

    @Override
    public Member registerSelf(MemberSignupDto dto) {
        ensureUniqueMember(dto.getEmail(), dto.getPhoneNumber());
        Member member = createMember(dto.getFullName(), dto.getEmail(), dto.getPhoneNumber(),
                dto.getDateOfBirth(), dto.getAddress());
        createMemberAccount(member, dto.getPassword());
        return member;
    }

    private void ensureUniqueMember(String email, String phoneNumber) {
        if (memberRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("A member with this email already exists.");
        }
        if (memberRepository.existsByPhoneNumber(phoneNumber)) {
            throw new IllegalArgumentException("A member with this phone number already exists.");
        }
    }

    private Member createMember(String fullName, String email, String phoneNumber,
                                LocalDate dateOfBirth, String address) {
        Member member = Member.builder()
                .fullName(fullName)
                .email(email)
                .phoneNumber(phoneNumber)
                .dateOfBirth(dateOfBirth)
                .address(address)
                .accountStatus(AccountStatus.ACTIVE)
                .registrationDate(LocalDate.now())
                .build();

        member = memberRepository.save(member);

        String memberId = "GYM-" + String.format("%05d", member.getId());
        member.setMemberId(memberId);
        member = memberRepository.save(member);

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

    private void createMemberAccount(Member member, String rawPassword) {
        if (!appUserRepository.existsByUsername(member.getEmail())) {
            AppUser appUser = AppUser.builder()
                    .username(member.getEmail())
                    .password(passwordEncoder.encode(rawPassword))
                    .role(UserRole.MEMBER)
                    .enabled(true)
                    .member(member)
                    .build();
            appUserRepository.save(appUser);
        }
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
