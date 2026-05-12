package com.example.demo.repository;

import com.example.demo.model.GymClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GymClassRepository extends JpaRepository<GymClass, Long> {

    List<GymClass> findByCancelledFalseOrderByScheduleDateTimeAsc();

    @Query("SELECT g FROM GymClass g WHERE g.cancelled = false AND g.scheduleDateTime >= :now ORDER BY g.scheduleDateTime ASC")
    List<GymClass> findUpcomingClasses(@Param("now") LocalDateTime now);

    List<GymClass> findByCreatedBy(String createdBy);
}
