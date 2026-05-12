package com.example.demo.repository;

import com.example.demo.model.AttendanceRecord;
import com.example.demo.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Long> {

    List<AttendanceRecord> findByMemberOrderByDateDescTimeDesc(Member member);

    List<AttendanceRecord> findByDate(LocalDate date);

    List<AttendanceRecord> findByDateOrderByTimeAsc(LocalDate date);

    boolean existsByMemberAndDate(Member member, LocalDate date);

    long countByDate(LocalDate date);
}
