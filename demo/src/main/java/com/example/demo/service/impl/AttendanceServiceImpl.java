package com.example.demo.service.impl;

import com.example.demo.dto.AttendanceDto;
import com.example.demo.model.AttendanceRecord;
import com.example.demo.model.Member;
import com.example.demo.repository.AttendanceRecordRepository;
import com.example.demo.repository.MemberRepository;
import com.example.demo.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRecordRepository attendanceRepository;
    private final MemberRepository memberRepository;

    @Override
    public AttendanceRecord markAttendance(AttendanceDto dto) {
        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        if (attendanceRepository.existsByMemberAndDate(member, dto.getDate())) {
            throw new IllegalStateException("Attendance already marked for this member today.");
        }

        AttendanceRecord record = AttendanceRecord.builder()
                .member(member)
                .date(dto.getDate())
                .time(dto.getTime())
                .notes(dto.getNotes())
                .build();

        return attendanceRepository.save(record);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceRecord> findByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        return attendanceRepository.findByMemberOrderByDateDescTimeDesc(member);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceRecord> findByDate(LocalDate date) {
        return attendanceRepository.findByDateOrderByTimeAsc(date);
    }

    @Override
    @Transactional(readOnly = true)
    public long countTodayVisits() {
        return attendanceRepository.countByDate(LocalDate.now());
    }
}
