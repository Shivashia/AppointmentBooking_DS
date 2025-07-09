package com.Devup.Patient.Repository;

import com.Devup.Patient.Entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository  extends JpaRepository<Patient,Integer> {
    Optional<Patient> findByEmail(String email);
}
