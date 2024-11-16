package com.example.VHV_backend.repo;

import com.example.VHV_backend.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    // You can add custom query methods if needed
}