package com.example.demo.model;

import com.example.demo.model.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "member_status_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    private AccountStatus oldStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus newStatus;

    @Column(nullable = false)
    private LocalDateTime changedAt;

    private String reason;
}
