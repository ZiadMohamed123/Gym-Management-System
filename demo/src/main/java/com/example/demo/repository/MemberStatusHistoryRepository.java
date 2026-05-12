package com.example.demo.repository;

import com.example.demo.model.Member;
import com.example.demo.model.MemberStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberStatusHistoryRepository extends JpaRepository<MemberStatusHistory, Long> {

    List<MemberStatusHistory> findByMemberOrderByChangedAtDesc(Member member);
}
