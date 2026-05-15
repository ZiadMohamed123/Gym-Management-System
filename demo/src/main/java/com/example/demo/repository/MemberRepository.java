package com.example.demo.repository;

import com.example.demo.model.Member;
import com.example.demo.model.enums.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByMemberId(String memberId);

    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    List<Member> findByAccountStatus(AccountStatus status);

    @Query("SELECT m FROM Member m WHERE " +
           "LOWER(m.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(m.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(m.memberId) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "m.phoneNumber LIKE CONCAT('%', :keyword, '%')")
    List<Member> searchByKeyword(@Param("keyword") String keyword);

    long countByAccountStatus(AccountStatus status);

    List<Member> findTop5ByOrderByIdDesc();
}
