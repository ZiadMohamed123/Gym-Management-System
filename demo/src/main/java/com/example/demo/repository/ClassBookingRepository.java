package com.example.demo.repository;

import com.example.demo.model.ClassBooking;
import com.example.demo.model.GymClass;
import com.example.demo.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassBookingRepository extends JpaRepository<ClassBooking, Long> {

    List<ClassBooking> findByGymClass(GymClass gymClass);

    List<ClassBooking> findByMember(Member member);

    Optional<ClassBooking> findByGymClassAndMember(GymClass gymClass, Member member);

    boolean existsByGymClassAndMember(GymClass gymClass, Member member);

    long countByGymClass(GymClass gymClass);
}
