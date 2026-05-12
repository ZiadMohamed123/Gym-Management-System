package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "gym_classes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GymClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private LocalDateTime scheduleDateTime;

    @Column(nullable = false)
    private int maxCapacity;

    private boolean cancelled;

    private String createdBy; // trainer or admin name

    @OneToMany(mappedBy = "gymClass", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ClassBooking> bookings = new ArrayList<>();

    // Convenience method: current booked count
    @Transient
    public int getBookedCount() {
        return bookings == null ? 0 : bookings.size();
    }

    @Transient
    public boolean isFull() {
        return getBookedCount() >= maxCapacity;
    }

    @Transient
    public int getAvailableSpots() {
        return maxCapacity - getBookedCount();
    }
}
