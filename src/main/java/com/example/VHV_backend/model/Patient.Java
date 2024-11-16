package com.example.VHV_backend.model;

import javax.persistence.*;

@Entity
@Table(name = "patients")
public class Patient {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String middleName;
    private String birthdate;
    private int age;
    private String patientType;
    private String diseaseType;
    private String passportNum;
    private String address;
    private String phoneNumber;

    // Getters and setters
    // ...
}