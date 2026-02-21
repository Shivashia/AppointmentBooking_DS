package com.Devup.UserService.Controller;

import com.Devup.UserService.DTO.*;
import com.Devup.UserService.Entity.Users;
import com.Devup.UserService.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid UsersRegistrationDTO dto){
        System.out.println("UserService "+dto.toString());
        Users savedUser=userService.registerUser(dto);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> loginUser(@RequestBody LoginDTO dto){
        JwtResponseDTO responseDTO= userService.login(dto);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDetailsDTO> getPatientByEmail(@PathVariable String email){
       UserDetailsDTO returnUser =userService.getUsersByEmail(email);
//        PatientDTO patientDTO=new PatientDTO(patient.getName(),patient.getEmail(), patient.getPhone(), patient.getId());
        System.out.println(returnUser.toString());
        return ResponseEntity.ok(returnUser);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<UserDetailsDTO> getUsersById(@PathVariable int id){
        UserDetailsDTO returnUser=userService.getUserById(id);
        System.out.println(returnUser.toString());
        return ResponseEntity.ok(returnUser);
    }

    @GetMapping("/getAllDocs")
    public ResponseEntity<List<DoctorDTO>> getAllDocs(){
        List<DoctorDTO> resList= userService.getAllDocs();
        return ResponseEntity.ok(resList);
    }

}
