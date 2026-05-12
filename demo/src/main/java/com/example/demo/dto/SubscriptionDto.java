package com.example.demo.dto;

import com.example.demo.model.enums.PaymentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class SubscriptionDto {

    private Long id;

    @NotNull(message = "Member is required")
    private Long memberId;

    @NotNull(message = "Plan is required")
    private Long planId;

    @NotNull(message = "Start date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull(message = "Payment status is required")
    private PaymentStatus paymentStatus;
}
