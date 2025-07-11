package com.DevUp.DoctorUp.Service;

import com.DevUp.DoctorUp.DTO.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class DoctorService {

    @Value("${doctor.service.url:http://DOCTOR-SERVICE}")
    private String doctorServiceUrl;

    private final RestTemplate restTemplate;

    private String appointmentServiceURL="http://APPOINTMENT-SERVICE";

    public DoctorService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String registerDoctor(RegisterDoctorDTO doctorDTO){
        RegisterDoctorDTO registeredDoctor = restTemplate.postForObject(
                doctorServiceUrl + "/doctor/register", doctorDTO, RegisterDoctorDTO.class);
        return "Doctor Registered: " + registeredDoctor.toString();
    }

    public String loginDoctor(LoginDTO loginDTO){
        try{
            LoginDTO loggedInDTO=restTemplate.postForObject(
                    doctorServiceUrl + "/doctor/login", loginDTO, LoginDTO.class);
            return "Doctor LoggedIn: " + loggedInDTO.toString();
        }catch (Exception e){
            throw new RuntimeException("Invalid doctor login");
        }

    }

    public List<DoctorDTO> getAllDoctors(String token){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<List<DoctorDTO>> response = restTemplate.exchange(
                doctorServiceUrl + "/doctor/all",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<DoctorDTO>>() {}
        );
        return response.getBody();
    }

    public DoctorDTO getDoctorByEmail(String token, String email){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        System.out.println("doctor Email: "+email);
        ResponseEntity<DoctorDTO> response = restTemplate.exchange(
                doctorServiceUrl + "/doctor/email/" + email,
                HttpMethod.GET,
                entity,
                DoctorDTO.class
        );
        System.out.println("Response entity: "+response.getBody().getEmail());

        return response.getBody();
    }

    public List<AppointmentDetailsDTO> getDoctorAppointments(int id, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<AppointmentDetailsDTO[]> response = restTemplate.exchange(
                appointmentServiceURL + "/appointments/getDoctorAppointment/" + id,
                HttpMethod.GET,
                requestEntity,
                AppointmentDetailsDTO[].class
        );
        System.out.println("Details: "+response.getBody());
        return Arrays.asList(response.getBody());
    }
}
