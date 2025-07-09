package com.Devup.Doctor.Service;

import com.Devup.Doctor.DTO.DoctorRegisterDTO;
import com.Devup.Doctor.Entity.Doctor;
import com.Devup.Doctor.Config.InvalidCredentialsException;
import com.Devup.Doctor.Repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {

    @Autowired
    private  DoctorRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Doctor register(DoctorRegisterDTO dto) {
        Doctor doctor = new Doctor();
        doctor.setName(dto.getName());
        doctor.setEmail(dto.getEmail());
        doctor.setPassword(passwordEncoder.encode(dto.getPassword()));
        doctor.setSpecialization(dto.getSpecialization());
        return repository.save(doctor);
    }



    public List<Doctor> getAll() {
        return repository.findAll();
    }

    public Doctor getById(int id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Doctor not found"));
    }
    public Doctor login(String email, String password) {
        Doctor doctor = repository.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(password, doctor.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        return doctor;
    }



}

