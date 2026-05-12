package com.example.demo.service.impl;

import com.example.demo.model.ClassBooking;
import com.example.demo.model.GymClass;
import com.example.demo.model.Member;
import com.example.demo.repository.ClassBookingRepository;
import com.example.demo.repository.GymClassRepository;
import com.example.demo.repository.MemberRepository;
import com.example.demo.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {

    private final ClassBookingRepository bookingRepository;
    private final GymClassRepository gymClassRepository;
    private final MemberRepository memberRepository;

    @Override
    public ClassBooking bookClass(Long classId, Long memberId) {
        GymClass gymClass = gymClassRepository.findById(classId)
                .orElseThrow(() -> new IllegalArgumentException("Class not found"));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        if (gymClass.isCancelled()) {
            throw new IllegalStateException("This class has been cancelled.");
        }

        if (bookingRepository.existsByGymClassAndMember(gymClass, member)) {
            throw new IllegalStateException("You have already booked this class.");
        }

        long currentCount = bookingRepository.countByGymClass(gymClass);
        if (currentCount >= gymClass.getMaxCapacity()) {
            throw new IllegalStateException("This class is fully booked. No available spots.");
        }

        ClassBooking booking = ClassBooking.builder()
                .gymClass(gymClass)
                .member(member)
                .bookedAt(LocalDateTime.now())
                .build();

        return bookingRepository.save(booking);
    }

    @Override
    public void cancelBooking(Long bookingId) {
        bookingRepository.deleteById(bookingId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClassBooking> getParticipants(Long classId) {
        GymClass gymClass = gymClassRepository.findById(classId)
                .orElseThrow(() -> new IllegalArgumentException("Class not found"));
        return bookingRepository.findByGymClass(gymClass);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClassBooking> getMemberBookings(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        return bookingRepository.findByMember(member);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isAlreadyBooked(Long classId, Long memberId) {
        GymClass gymClass = gymClassRepository.findById(classId).orElse(null);
        Member member = memberRepository.findById(memberId).orElse(null);
        if (gymClass == null || member == null) return false;
        return bookingRepository.existsByGymClassAndMember(gymClass, member);
    }
}
