package com.DevUp.DoctorUp.Service;

import com.DevUp.DoctorUp.DTO.AppointmentBookingDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AppointmentService {

    private String appointmentServiceUrl="http://APPOINTMENT-SERVICE";

    private final RestTemplate restTemplate;

    public AppointmentService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void bookAppointment(AppointmentBookingDTO bookingDTO, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AppointmentBookingDTO> entity = new HttpEntity<>(bookingDTO, headers);

        restTemplate.postForEntity(
                appointmentServiceUrl + "/appointments/register",
                entity,
                Void.class
        );
    }



}
