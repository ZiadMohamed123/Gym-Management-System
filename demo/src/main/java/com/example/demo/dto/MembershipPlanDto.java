package com.example.demo.dto;

import com.example.demo.model.enums.MembershipDuration;
import com.example.demo.model.enums.MembershipStatus;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MembershipPlanDto {

    private Long id;

    @NotBlank(message = "Plan name is required")
    @Size(min = 2, max = 100)
    private String name;

    @Size(max = 500)
    private String description;

    @NotNull(message = "Duration is required")
    private MembershipDuration duration;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal price;

    @NotNull(message = "Status is required")
    private MembershipStatus status;
}
