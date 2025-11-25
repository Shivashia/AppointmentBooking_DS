package com.Devup.UserService.Controller;

import com.Devup.UserService.DTO.UsersRegistrationDTO;
import com.Devup.UserService.Entity.Users;
import com.Devup.UserService.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
}
