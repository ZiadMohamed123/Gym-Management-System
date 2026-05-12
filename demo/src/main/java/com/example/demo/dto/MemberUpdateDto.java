package com.example.demo.dto;

import com.example.demo.model.enums.AccountStatus;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class MemberUpdateDto {

    @NotNull(message = "Member ID is required")
    private Long id;

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100)
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[+]?[0-9]{10,15}$")
    private String phoneNumber;

    @NotNull(message = "Date of birth is required")
    @Past
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Address is required")
    private String address;

    @NotNull(message = "Account status is required")
    private AccountStatus accountStatus;

    private String statusChangeReason;
}
