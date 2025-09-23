package com.eureka.tarea1_api.dto;

import java.time.LocalDate;
import lombok.Data;


@Data
public class CandidateDTO {
    private Integer id;
    private String name;
    private String lastName;
    private String email;
    private String phone;
    private String documentType;
    private String documentNumber;
    private String gender;
    private String placeOfBirth;
    private LocalDate dateOfBirth;
    private String address;
    private String postalCode;
    private String country;
    private String localization;
    private LocalDate availableStartDate;
    private LocalDate availableEndDate;
}