package com.DevUp.AuthService.Controller;

import com.DevUp.AuthService.Config.JwtUtil;
import com.DevUp.AuthService.DTO.JwtResponseDTO;
import com.DevUp.AuthService.DTO.LoginDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;

@RestController
@RequestMapping("/auth")

public class AuthController {

    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Value("${doctor.service.url:http://DOCTOR-SERVICE}")
    private String doctorServiceUrl;

    @Value("${patient.service.url:http://PATIENT-SERVICE}")
    private String patientServiceUrl;

    public AuthController(RestTemplate restTemplate, JwtUtil jwtUtil) {
        this.restTemplate = restTemplate;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping(value = "/login", produces = "application/json")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody LoginDTO dto, @RequestParam String role) {
        try {
            String loginUrl;

            if ("DOCTOR".equalsIgnoreCase(role)) {
                loginUrl = doctorServiceUrl + "/doctor/login";
            } else if ("PATIENT".equalsIgnoreCase(role)) {
                loginUrl = patientServiceUrl + "/patient/login";
            } else {
                log.warn("Invalid role provided: {}", role);
                return ResponseEntity.badRequest().build();
            }
            ResponseEntity<?> response = restTemplate.postForEntity(loginUrl, dto, Object.class);
            System.out.println("Response Entity "+response.getBody());
            log.info("Patient service responded: {}", response.getBody());
//            ResponseEntity<Map> response = restTemplate.postForEntity(loginUrl, dto, Map.class);

            String token = jwtUtil.generateToken(dto.getEmail(), role.toUpperCase());
            System.out.println("TOKEN FOR JWT "+token);
            return ResponseEntity.ok(new JwtResponseDTO(token, role.toUpperCase()));

        } catch (HttpClientErrorException ex) {
            log.error("Client error during authentication: {}", ex.getMessage());
            return ResponseEntity.status(ex.getStatusCode()).build();

        } catch (HttpServerErrorException ex) {
            log.error("Server error during authentication: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        } catch (Exception e) {
            log.error("Unexpected error during authentication", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



}
