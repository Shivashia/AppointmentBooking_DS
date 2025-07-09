package com.DevUp.DoctorUp.Service;

import com.DevUp.DoctorUp.DTO.JwtResponseDTO;
import com.DevUp.DoctorUp.DTO.LoginDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class AuthService {

    @Value("${auth.service.url:http://AUTH-SERVICE}")
    public String authServiceUrl;

    private final RestTemplate restTemplate;

    public AuthService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public JwtResponseDTO authenticateUser(LoginDTO loginDTO, String role) {
        String url = authServiceUrl + "/auth/login?role=" + role;

        // Prepare headers to ensure proper Content-Type and Accept
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // Wrap the login data and headers into a request entity
        HttpEntity<LoginDTO> requestEntity = new HttpEntity<>(loginDTO, headers);

        try {
            // Make the POST request to the Auth service
            ResponseEntity<JwtResponseDTO> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    JwtResponseDTO.class
            );

            // Return token if successful
            return response.getBody();

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            // Handle 4xx or 5xx errors explicitly
            System.err.println("Authentication failed: " + ex.getResponseBodyAsString());
            throw new RuntimeException("Invalid credentials or server error");

        } catch (Exception e) {
            // Handle other unexpected errors
            System.err.println("Auth service error: " + e.getMessage());
            throw new RuntimeException("Unable to connect to Auth Service");
        }
    }


}

