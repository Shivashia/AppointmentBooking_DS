package com.DevUp.NotificationService.Listener;

import com.DevUp.NotificationService.DTO.DoctorDTO;
import com.DevUp.NotificationService.DTO.PatientDTO;
import com.DevUp.NotificationService.Service.EmailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class AppointmentEventListener {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmailService emailService;

    private final RestTemplate restTemplate;

    public AppointmentEventListener(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Value("${doctor.service.url:http://DOCTOR-SERVICE}")
    private String doctorServiceUrl;

    @Value("${patient.service.url:http://PATIENT-SERVICE}")
    private String patientServiceUrl;



    @JmsListener(destination = "appointment.queue")
    public void handleAppointmentEvent(String message) {
        try {
            AppointmentEvent event = objectMapper.readValue(message, AppointmentEvent.class);
            log.info("Received Appointment Event: {}", event);
            String token = event.getToken();

            switch (event.getStatus()) {
                case "PENDING":
                    notifyDoctor(event,token);
                    break;
                case "APPROVED":
                    notifyPatientApproval(event,token);
                    break;
                case "REJECTED":
                    notifyPatientRejection(event,token);
                    break;
                default:
                    log.warn("Unknown event status: {}", event.getStatus());
            }
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize AppointmentEvent", e);
        }
    }

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy 'at' hh:mm a");

    private void notifyDoctor(AppointmentEvent event,String serviceAuthToken) {
        String doctorEmail = getDoctorEmail(event.getDoctorId(), serviceAuthToken);
        String subject = "New Appointment Request";
        String body = String.format(
                "Dear Doctor, you have a new appointment request from patient ID: %s scheduled for %s.",
                event.getPatientId(),
                event.getAppointmentDateTime() != null
                        ? event.getAppointmentDateTime().format(dateTimeFormatter)
                        : "N/A"
        );
        emailService.sendEmail(doctorEmail, subject, body);
        log.info("Email sent to doctor {} for pending appointment.", doctorEmail);
    }

    private void notifyPatientApproval(AppointmentEvent event,String serviceAuthToken) {
        String patientEmail = getPatientEmail(event.getPatientId(), serviceAuthToken);
        String subject = "Appointment Approved";
        String body = String.format(
                "Hello, your appointment %s with Doctor ID: %s scheduled for %s has been approved.",
                event.getAppointmentId(),
                event.getDoctorId(),
                event.getAppointmentDateTime() != null
                        ? event.getAppointmentDateTime().format(dateTimeFormatter)
                        : "N/A"
        );
        emailService.sendEmail(patientEmail, subject, body);
        log.info("Email sent to patient {} for approval.", patientEmail);
    }

    private void notifyPatientRejection(AppointmentEvent event,String serviceAuthToken) {
        String patientEmail = getPatientEmail(event.getPatientId(), serviceAuthToken);
        String subject = "Appointment Rejected";
        String body = String.format(
                "Hello, your appointment %s with Doctor ID: %s scheduled for %s has been rejected.",
                event.getAppointmentId(),
                event.getDoctorId(),
                event.getAppointmentDateTime() != null
                        ? event.getAppointmentDateTime().format(dateTimeFormatter)
                        : "N/A"
        );
        emailService.sendEmail(patientEmail, subject, body);
        log.info("Email sent to patient {} for rejection.", patientEmail);
    }

    private String getDoctorEmail(int doctorId, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<DoctorDTO> doctorResponse = restTemplate.exchange(
                doctorServiceUrl + "/doctor/" + doctorId,
                HttpMethod.GET,
                request,
                DoctorDTO.class
        );

        if (doctorResponse.getBody() != null) {
            return doctorResponse.getBody().getEmail();
        }
        throw new RuntimeException("Doctor email not found for ID: " + doctorId);
    }

    private String getPatientEmail(int patientId, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<PatientDTO> patientResponse = restTemplate.exchange(
                patientServiceUrl + "/patient/" + patientId,
                HttpMethod.GET,
                request,
                PatientDTO.class
        );

        if (patientResponse.getBody() != null) {
            return patientResponse.getBody().getEmail();
        }
        throw new RuntimeException("Patient email not found for ID: " + patientId);
    }
}
