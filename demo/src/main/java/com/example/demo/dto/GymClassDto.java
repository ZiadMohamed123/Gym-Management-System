package com.example.demo.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class GymClassDto {

    private Long id;

    @NotBlank(message = "Class name is required")
    @Size(min = 2, max = 100)
    private String name;

    @Size(max = 1000)
    private String description;

    @NotNull(message = "Schedule date and time is required")
    @Future(message = "Schedule must be in the future")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime scheduleDateTime;

    @NotNull(message = "Max capacity is required")
    @Min(value = 1, message = "Capacity must be at least 1")
    @Max(value = 500, message = "Capacity cannot exceed 500")
    private Integer maxCapacity;

    private String createdBy;
}
