package com.Devup.Patient.Controller;

import com.Devup.Patient.DTO.JwtResponseDTO;
import com.Devup.Patient.DTO.LoginDTO;
import com.Devup.Patient.DTO.PatientDTO;
import com.Devup.Patient.DTO.PatientRegisterDTO;
import com.Devup.Patient.Entity.Patient;
import com.Devup.Patient.Service.PatientService;
import com.Devup.Patient.config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    private PatientService service;

    @Autowired
    private JwtUtil jwtUtil;



    @PostMapping("/register")
    public ResponseEntity<Patient> register(@RequestBody @Valid PatientRegisterDTO dto) {
        Patient patient = service.registerPatient(dto);
        return new ResponseEntity<>(patient, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Patient>> getAll() {
        return ResponseEntity.ok(service.getAllPatients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getById(@PathVariable int id) {
        return ResponseEntity.ok(service.getPatientById(id));
    }



//    @PostMapping("/login")
//    public ResponseEntity<Patient> login(@RequestBody LoginDTO loginDTO) {
//
//        Patient patient = service.login(loginDTO.getEmail(), loginDTO.getPassword());
//
//        return ResponseEntity.ok(patient);
//    }

//    @PostMapping("/login")
//    public ResponseEntity<JwtResponseDTO> login(@RequestBody LoginDTO loginDTO) {
//        Patient patient = service.login(loginDTO.getEmail(), loginDTO.getPassword());
//        String token = jwtUtil.generateToken(patient.getEmail(), "PATIENT");
//        return ResponseEntity.ok(new JwtResponseDTO(token, "PATIENT"));
//    }

//    @PostMapping("/login")
//    public ResponseEntity<Map<String, String>> login(@RequestBody LoginDTO loginDTO) {
//        Patient patient = service.login(loginDTO.getEmail(), loginDTO.getPassword());
//        String token = jwtUtil.generateToken(patient.getEmail(), "PATIENT");
//
//        Map<String, String> response = new HashMap<>();
//        response.put("token", token);
//        response.put("role", "PATIENT");
//        System.out.println(response);
//        return ResponseEntity.ok(response);
//    }

    @PostMapping("/login")
    public ResponseEntity<Patient> login(@RequestBody LoginDTO loginDTO) {
        Patient patient = service.login(loginDTO.getEmail(), loginDTO.getPassword());
        return ResponseEntity.ok(patient); // ✅ NO TOKEN
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<PatientDTO> getPatientByEmail(@PathVariable String email){
        Patient patient=service.getPatientbyEmail(email);
        PatientDTO patientDTO=new PatientDTO(patient.getName(),patient.getEmail(), patient.getPhone(), patient.getId());
        System.out.println(patientDTO.toString());
        return ResponseEntity.ok(patientDTO);
    }


}


