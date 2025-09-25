package com.eureka.tarea1_api.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CandidateDTO {
    private Integer id;

    @NotBlank(message = "Name is required")
    @Size(max = 50, message = "Name must be at most 50 characters")
    private String name;

    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name must be at most 50 characters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 150, message = "Email must be at most 150 characters")
    private String email;

    @NotBlank(message = "Phone is required")
    @Size(max = 20, message = "Phone must be at most 20 characters")
    private String phone;

    @NotBlank(message = "Document type is required")
    @Size(max = 15, message = "Document type must be at most 15 characters")
    private String documentType;

    @NotBlank(message = "Document number is required")
    @Size(max = 30, message = "Document number must be at most 30 characters")
    private String documentNumber;

    @NotBlank(message = "Gender is required")
    @Size(max = 20, message = "Gender must be at most 20 characters")
    private String gender;

    @NotBlank(message = "Place of birth is required")
    @Size(max = 200, message = "Place of birth must be at most 200 characters")
    private String placeOfBirth;

    @NotNull(message = "Date of birth is required")
    @PastOrPresent(message = "Date of birth cannot be in the future")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Address is required")
    @Size(max = 200, message = "Address must be at most 200 characters")
    private String address;

    @NotBlank(message = "Postal code is required")
    @Size(max = 20, message = "Postal code must be at most 20 characters")
    private String postalCode;

    @NotBlank(message = "Country is required")
    @Size(max = 50, message = "Country must be at most 50 characters")
    private String country;

    @NotBlank(message = "Localization is required")
    @Size(max = 150, message = "Localization must be at most 150 characters")
    private String localization;

    @NotNull(message = "Available start date is required")
    private LocalDate availableStartDate;

    @NotNull(message = "Available end date is required")
    private LocalDate availableEndDate;
}
