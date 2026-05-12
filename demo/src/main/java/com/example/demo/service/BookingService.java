package com.example.demo.service;

import com.example.demo.model.ClassBooking;

import java.util.List;

public interface BookingService {
    ClassBooking bookClass(Long classId, Long memberId);
    void cancelBooking(Long bookingId);
    List<ClassBooking> getParticipants(Long classId);
    List<ClassBooking> getMemberBookings(Long memberId);
    boolean isAlreadyBooked(Long classId, Long memberId);
}
