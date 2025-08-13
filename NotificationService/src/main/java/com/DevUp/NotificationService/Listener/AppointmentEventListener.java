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

//    private void notifyDoctor(AppointmentEvent event,String serviceAuthToken) {
//        PatientDTO p_details=getPatientDetails(event.getPatientId(),serviceAuthToken);
//        DoctorDTO d_details = getDoctorDetails(event.getDoctorId(), serviceAuthToken);
//        String subject = "New Appointment Request";
//        String body = String.format(
//                "Dear Doctor, you have a new appointment request from patient: %s scheduled for %s.",
//                p_details.getName(),
//                event.getAppointmentDateTime() != null
//                        ? event.getAppointmentDateTime().format(dateTimeFormatter)
//                        : "N/A"
//        );
//        emailService.sendEmail(d_details.getEmail(), subject, body);
//        log.info("Email sent to doctor {} for pending appointment.", d_details.getEmail());
//    }
//
//    private void notifyPatientApproval(AppointmentEvent event,String serviceAuthToken) {
//        PatientDTO p_details=getPatientDetails(event.getPatientId(),serviceAuthToken);
//        DoctorDTO d_details = getDoctorDetails(event.getDoctorId(), serviceAuthToken);
//        String subject = "Appointment Approved";
//        String body = String.format(
//                "Hello, your appointment with Doctor : %s scheduled for %s has been approved.",
//                d_details.getName(),
//                event.getAppointmentDateTime() != null
//                        ? event.getAppointmentDateTime().format(dateTimeFormatter)
//                        : "N/A"
//        );
//        emailService.sendEmail(p_details.getEmail(), subject, body);
//        log.info("Email sent to patient {} for approval.", p_details.getEmail());
//    }
//
//    private void notifyPatientRejection(AppointmentEvent event,String serviceAuthToken) {
//        PatientDTO p_details=getPatientDetails(event.getPatientId(),serviceAuthToken);
//        DoctorDTO d_details = getDoctorDetails(event.getDoctorId(), serviceAuthToken);
//        String subject = "Appointment Rejected";
//        String body = String.format(
//                "Hello, your appointment with Doctor ID: %s scheduled for %s has been rejected.",
//                d_details.getName(),
//                event.getAppointmentDateTime() != null
//                        ? event.getAppointmentDateTime().format(dateTimeFormatter)
//                        : "N/A"
//        );
//        emailService.sendEmail(p_details.getEmail(), subject, body);
//        log.info("Email sent to patient {} for rejection.", p_details.getEmail());
//    }

    private void notifyDoctor(AppointmentEvent event, String serviceAuthToken) {
        PatientDTO p_details = getPatientDetails(event.getPatientId(), serviceAuthToken);
        DoctorDTO d_details = getDoctorDetails(event.getDoctorId(), serviceAuthToken);
        String subject = "New Appointment Request";
        String appointmentTime = event.getAppointmentDateTime() != null
                ? event.getAppointmentDateTime().format(dateTimeFormatter)
                : "N/A";
        String body = buildNewAppointmentHtml(d_details.getName(), p_details.getName(), appointmentTime);

        emailService.sendEmailHtml(d_details.getEmail(), subject, body);
        log.info("HTML email sent to doctor {} for pending appointment.", d_details.getEmail());
    }

    private void notifyPatientApproval(AppointmentEvent event, String serviceAuthToken) {
        PatientDTO p_details = getPatientDetails(event.getPatientId(), serviceAuthToken);
        DoctorDTO d_details = getDoctorDetails(event.getDoctorId(), serviceAuthToken);
        String subject = "Appointment Approved";
        String appointmentTime = event.getAppointmentDateTime() != null
                ? event.getAppointmentDateTime().format(dateTimeFormatter)
                : "N/A";
        String body = buildAppointmentApprovedHtml(p_details.getName(), d_details.getName(), appointmentTime);

        emailService.sendEmailHtml(p_details.getEmail(), subject, body);
        log.info("HTML email sent to patient {} for approval.", p_details.getEmail());
    }

    private void notifyPatientRejection(AppointmentEvent event, String serviceAuthToken) {
        PatientDTO p_details = getPatientDetails(event.getPatientId(), serviceAuthToken);
        DoctorDTO d_details = getDoctorDetails(event.getDoctorId(), serviceAuthToken);
        String subject = "Appointment Rejected";
        String appointmentTime = event.getAppointmentDateTime() != null
                ? event.getAppointmentDateTime().format(dateTimeFormatter)
                : "N/A";
        String body = buildAppointmentRejectedHtml(p_details.getName(), d_details.getName(), appointmentTime);

        emailService.sendEmailHtml(p_details.getEmail(), subject, body);
        log.info("HTML email sent to patient {} for rejection.", p_details.getEmail());
    }

    private DoctorDTO getDoctorDetails(int doctorId, String token) {
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
            return doctorResponse.getBody();
        }
        throw new RuntimeException("Doctor email not found for ID: " + doctorId);
    }

    private PatientDTO getPatientDetails(int patientId, String token) {
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
            return patientResponse.getBody();
        }
        throw new RuntimeException("Patient email not found for ID: " + patientId);
    }


    private String buildNewAppointmentHtml(String doctorName, String patientName, String appointmentDateTime) {
        return """
        <html>
        <body style="font-family: Arial, sans-serif; color: #333;">
            <h2 style="color: #2C3E50;">New Appointment Request</h2>
            <p>Dear Dr. %s,</p>
            <p>You have a new appointment request from patient <strong>%s</strong> scheduled for:</p>
            <p style="font-size: 16px; color: #16A085;"><strong>%s</strong></p>
            <hr/>
            <p style="font-size: 12px; color: #7F8C8D;">This is an automated message from the Doctor Appointment Booking System.</p>
        </body>
        </html>
        """.formatted(doctorName, patientName, appointmentDateTime);
    }

    private String buildAppointmentApprovedHtml(String patientName, String doctorName, String appointmentDateTime) {
        return """
        <html>
        <body style="font-family: Arial, sans-serif; color: #333;">
            <h2 style="color: #27AE60;">Appointment Approved</h2>
            <p>Hello %s,</p>
            <p>Your appointment with Dr. <strong>%s</strong> scheduled for:</p>
            <p style="font-size: 16px; color: #2980B9;"><strong>%s</strong></p>
            <p>has been <strong style="color: #27AE60;">approved</strong>.</p>
            <hr/>
            <p style="font-size: 12px; color: #7F8C8D;">Please arrive 10 minutes early to your appointment.</p>
        </body>
        </html>
        """.formatted(patientName, doctorName, appointmentDateTime);
    }

    private String buildAppointmentRejectedHtml(String patientName, String doctorName, String appointmentDateTime) {
        return """
        <html>
        <body style="font-family: Arial, sans-serif; color: #333;">
            <h2 style="color: #C0392B;">Appointment Rejected</h2>
            <p>Hello %s,</p>
            <p>We regret to inform you that your appointment with Dr. <strong>%s</strong> scheduled for:</p>
            <p style="font-size: 16px; color: #C0392B;"><strong>%s</strong></p>
            <p>has been <strong style="color: #C0392B;">rejected</strong>.</p>
            <hr/>
            <p style="font-size: 12px; color: #7F8C8D;">Please contact the clinic for further assistance.</p>
        </body>
        </html>
        """.formatted(patientName, doctorName, appointmentDateTime);
    }
}
