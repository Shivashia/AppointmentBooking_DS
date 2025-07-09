package com.DevUp.DoctorUp.Service;

import com.DevUp.DoctorUp.DTO.AppointmentDetailsDTO;
import com.DevUp.DoctorUp.DTO.LoginDTO;
import com.DevUp.DoctorUp.DTO.PatientDTO;
import com.DevUp.DoctorUp.DTO.RegisterPatientDTO;
import com.Devup.Appointment.DTO.DoctorDTO;
//import com.Devup.Appointment.DTO.PatientDTO;
import com.Devup.Patient.Entity.Patient;
import com.Devup.Patient.Repository.PatientRepository;
import com.Devup.Patient.config.InvalidCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class PatientService {

    @Value("${patient.service.url:http://PATIENT-SERVICE}")
    private String patientServiceUrl;



    private String appointmentServiceURL="http://APPOINTMENT-SERVICE";

    private final RestTemplate restTemplate;

    public PatientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String registerPatient(RegisterPatientDTO patientDTO) {
        RegisterPatientDTO registeredPatient = restTemplate.postForObject(
                patientServiceUrl + "/patient/register", patientDTO, RegisterPatientDTO.class);
        return "Patient Registered: " + registeredPatient.toString();
    }

    public String loginPatient(LoginDTO loginDTO){
        try{
            System.out.println("Patient Service"+loginDTO.getPassword()+loginDTO.getEmail());
            LoginDTO loggedInDTO=restTemplate.postForObject(
                    patientServiceUrl + "/patient/login", loginDTO, LoginDTO.class);
            return "Patient LoggedIn: " + loggedInDTO.toString();
        }catch (Exception e){
            throw new RuntimeException("Invalid doctor login");
        }
    }

    public List<AppointmentDetailsDTO> getPatientAppointments(int id, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<AppointmentDetailsDTO[]> response = restTemplate.exchange(
                appointmentServiceURL + "/appointments/getPatientAppointment/" + id,
                HttpMethod.GET,
                requestEntity,
                AppointmentDetailsDTO[].class
        );
        System.out.println("Details: "+response.getBody());
        return Arrays.asList(response.getBody());
    }



    public PatientDTO getPatientByEmail(String email, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<PatientDTO> response = restTemplate.exchange(
                patientServiceUrl + "/patient/email/" + email,
                HttpMethod.GET,
                entity,
                PatientDTO.class
        );

        return response.getBody();
    }

}
