package com.example.demo.service;

import com.example.demo.dto.MemberRegistrationDto;
import com.example.demo.dto.MemberSignupDto;
import com.example.demo.dto.MemberUpdateDto;
import com.example.demo.model.Member;

import java.util.List;
import java.util.Optional;

public interface MemberService {

    Member register(MemberRegistrationDto dto);

    Member registerSelf(MemberSignupDto dto);

    Member update(MemberUpdateDto dto);

    Optional<Member> findById(Long id);

    Optional<Member> findByMemberId(String memberId);

    Optional<Member> findByEmail(String email);

    List<Member> findAll();

    List<Member> search(String keyword);

    void delete(Long id);

    long countTotal();

    long countActive();
}
