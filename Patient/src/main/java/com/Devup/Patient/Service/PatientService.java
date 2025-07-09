package com.Devup.Patient.Service;

import com.Devup.Patient.DTO.PatientRegisterDTO;
import com.Devup.Patient.Entity.Patient;
import com.Devup.Patient.Repository.PatientRepository;
import com.Devup.Patient.config.InvalidCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    @Autowired
    private PatientRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;



    public Patient registerPatient(PatientRegisterDTO dto) {
        Patient patient =new Patient();
        patient.setName(dto.getName());
        patient.setEmail(dto.getEmail());
        patient.setPhone(dto.getPhone());
        patient.setPassword(passwordEncoder.encode(dto.getPassword()));
        return repository.save(patient);
    }

    public List<Patient> getAllPatients() {
        return repository.findAll();
    }

    public Patient getPatientById(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
    }

    public Patient login(String email, String password) {
        Patient patient = repository.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));
        if (!passwordEncoder.matches(password, patient.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }
        System.out.println("Patient Found "+patient.getEmail());
        return patient;
    }

    public Patient getPatientbyEmail(String email){
        Patient patient = repository.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));
        return patient;
    }


}
