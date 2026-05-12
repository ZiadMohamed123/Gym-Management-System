package com.example.demo.service;

import com.example.demo.dto.AttendanceDto;
import com.example.demo.model.AttendanceRecord;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceService {
    AttendanceRecord markAttendance(AttendanceDto dto);
    List<AttendanceRecord> findByMemberId(Long memberId);
    List<AttendanceRecord> findByDate(LocalDate date);
    long countTodayVisits();
}
