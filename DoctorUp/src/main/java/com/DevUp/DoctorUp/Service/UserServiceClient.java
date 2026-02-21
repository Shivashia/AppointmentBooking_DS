package com.DevUp.DoctorUp.Service;

import com.DevUp.DoctorUp.DTO.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    private String appointmentServiceURL="http://APPOINTMENT-SERVICE";

    @Autowired
    private RestTemplate restTemplate;

    public void registerUser(UserRegistrationDTO dto) {
        System.out.println("Sent to User Service");
        restTemplate.postForObject(
                userServiceUrl + "/auth/register",
                dto,
                String.class
        );
    }

    public JwtResponseDTO loginUser(LoginDTO loginDTO) {
        return restTemplate.postForObject(
                userServiceUrl + "/auth/login",
                loginDTO,
                JwtResponseDTO.class
        );
    }

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

    public List<AppointmentDetailsDTO> getPatientAppointments(int id, String token) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        System.out.println("Sent to User Service for all docs");
        ResponseEntity<List<AppointmentDetailsDTO>> response =
                restTemplate.exchange(
                        appointmentServiceURL + "/appointments/getPatientAppointment/" + id,
                        HttpMethod.GET,
                        entity,
                        new ParameterizedTypeReference<List<AppointmentDetailsDTO>>() {}
                );

        return response.getBody();

    }

    public List<AppointmentDetailsDTO> getDoctorAppointments(int id, String token) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        System.out.println("Sent to User Service for all docs");
        ResponseEntity<List<AppointmentDetailsDTO>> response =
                restTemplate.exchange(
                        appointmentServiceURL + "/appointments/getDoctorAppointment/" + id,
                        HttpMethod.GET,
                        entity,
                        new ParameterizedTypeReference<List<AppointmentDetailsDTO>>() {}
                );

        return response.getBody();
    }
}
