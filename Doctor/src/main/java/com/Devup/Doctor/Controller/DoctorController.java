package com.Devup.Doctor.Controller;

import com.Devup.Doctor.Config.JwtUtil;
import com.Devup.Doctor.DTO.DoctorDTO;
import com.Devup.Doctor.DTO.DoctorRegisterDTO;
import com.Devup.Doctor.DTO.JwtResponseDTO;
import com.Devup.Doctor.DTO.LoginDTO;
import com.Devup.Doctor.Entity.Doctor;
import com.Devup.Doctor.Repository.DoctorRepository;
import com.Devup.Doctor.Service.DoctorService;
import com.netflix.discovery.converters.Auto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/doctor")
@RequiredArgsConstructor
public class DoctorController {

    @Autowired
    private DoctorService service;

    @Autowired
    private DoctorRepository repository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<Doctor> register(@RequestBody @Valid DoctorRegisterDTO dto) {
        Doctor doctor = service.register(dto);
        return new ResponseEntity<>(doctor, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public List<Doctor> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getById(@PathVariable int id) {
        return ResponseEntity.ok(service.getById(id));
    }

//    @PostMapping("/login")
//    public ResponseEntity<Doctor> login(@RequestBody LoginDTO loginDTO) {
//        Doctor doctor = service.login(loginDTO.getEmail(), loginDTO.getPassword());
//        return ResponseEntity.ok(doctor); // 🔁 no token here
//    }
//    @PostMapping("/login")
//    public ResponseEntity<JwtResponseDTO> login(@RequestBody LoginDTO loginDTO) {
//        Doctor doctor = service.login(loginDTO.getEmail(), loginDTO.getPassword());
//        String token = jwtUtil.generateToken(doctor.getEmail(), "DOCTOR");
//        return ResponseEntity.ok(new JwtResponseDTO(token, "DOCTOR"));
//    }

    @PostMapping("/login")
    public ResponseEntity<Doctor> login(@RequestBody LoginDTO loginDTO) {
        Doctor doctor = service.login(loginDTO.getEmail(), loginDTO.getPassword());
        return ResponseEntity.ok(doctor);
    }

//    @PostMapping("/login")
//    public ResponseEntity<Patient> login(@RequestBody LoginDTO loginDTO) {
//        Patient patient = service.login(loginDTO.getEmail(), loginDTO.getPassword());
//        return ResponseEntity.ok(patient); // ✅ NO TOKEN
//    }


    @GetMapping("/available")
    public List<Doctor> getAvailableDoctors() {
        return service.getAll();
    }

    @GetMapping(value = "/email/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DoctorDTO> getDoctorByEmail(@PathVariable String email) {
        Doctor doctor = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid Username or password"));
        DoctorDTO nDoc=new DoctorDTO();
        nDoc.setId(doctor.getId());
        nDoc.setEmail(doctor.getEmail());
        nDoc.setSpecialization(doctor.getSpecialization());
        nDoc.setName(doctor.getName());
        System.out.println(nDoc);
//        DoctorDTO newDoc = new DoctorDTO(doctor.getId(), doctor.getName(), doctor.getSpecialization(), doctor.getEmail());
        return ResponseEntity.ok().body(nDoc);
    }

}

