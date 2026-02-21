package com.Devup.Appointment.Service;

import com.Devup.Appointment.DTO.DoctorDTO;
import com.Devup.Appointment.DTO.UserDetailsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class UserServiceClient {
//    @Value("${user.service.url:http://USER-SERVICE}")
    private String userServiceUrl="http://USER-SERVICE";

    @Autowired
    private RestTemplate restTemplate;





    public UserDetailsDTO getUserbyEmail(String email, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<UserDetailsDTO> response = restTemplate.exchange(
                userServiceUrl + "/auth/email/" + email,
                HttpMethod.GET,
                entity,
                UserDetailsDTO.class
        );

        return response.getBody();
    }

    public List<DoctorDTO> getAllDoctors(String token) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        System.out.println("Sent to User Service for all docs");
        ResponseEntity<List<DoctorDTO>> response = restTemplate.exchange(
                userServiceUrl + "/auth/getAllDocs",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<DoctorDTO>>() {}
        );
        return response.getBody();
    }
}
