package com.DevUp.NotificationService.Listener;

import com.DevUp.NotificationService.DTO.DoctorDTO;
import com.DevUp.NotificationService.DTO.PatientDTO;
import com.DevUp.NotificationService.DTO.UserDetailsDTO;
import com.DevUp.NotificationService.Service.EmailService;
import com.DevUp.NotificationService.Util.EmailTemplateRenderer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
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

    private final EmailTemplateRenderer renderer;

    @Autowired
    public AppointmentEventListener(RestTemplate restTemplate, EmailTemplateRenderer renderer) {
        this.restTemplate = restTemplate;
        this.renderer = renderer;
    }

    @Value("${doctor.service.url:http://DOCTOR-SERVICE}")
    private String doctorServiceUrl;

    @Value("${patient.service.url:http://PATIENT-SERVICE}")
    private String patientServiceUrl;


    private String userServiceUrl="http://USER-SERVICE";


    @JmsListener(destination = "appointment.queue")
    public void handleAppointmentEvent(String message) {
        try {
            AppointmentEvent event = objectMapper.readValue(message, AppointmentEvent.class);
            log.info("Received Appointment Event: {}", event);

            try{
                emailService.logNotification(event);
            }catch (DataIntegrityViolationException e){

            }


            switch (event.getStatus().toString()) {
                case "PENDING":
                    notifyDoctor(event);
                    break;
                case "APPROVED":
                    notifyPatientApproval(event);
                    break;
                case "REJECTED":
                    notifyPatientRejection(event);
                    break;
                default:
                    log.warn("Unknown event status: {}", event.getStatus());
            }
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize AppointmentEvent", e);
        }
    }

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy 'at' hh:mm a");


    private void notifyDoctor(AppointmentEvent event) {
        //UserDetailsDTO p_details = getPatientDetails(event.getPatientId(), serviceAuthToken);
        //UserDetailsDTO d_details = getDoctorDetails(event.getDoctorId(), serviceAuthToken);
        String subject = "New Appointment Request";
        String appointmentTime = event.getAppointmentDateTime() != null
                ? event.getAppointmentDateTime().format(dateTimeFormatter)
                : "N/A";
        String body_doc = buildNewAppointmentHtmlDoc(event.getDoctorName(), event.getPatientName(), appointmentTime);

        String body_patient = buildNewAppointmentHtmlPatient(event.getDoctorName(), event.getPatientName(), appointmentTime);
        System.out.println(event.getDoctorEmail()+"  "+event.getPatientEmail());
        emailService.sendEmailHtml(event.getDoctorEmail(), subject, body_doc);
        emailService.sendEmailHtml(event.getPatientEmail(), subject, body_patient);
        log.info("HTML email sent to doctor {} for pending appointment.", event.getDoctorEmail());
    }



    private void notifyPatientApproval(AppointmentEvent event) {
        //UserDetailsDTO p_details = getPatientDetails(event.getPatientId(), serviceAuthToken);
        //UserDetailsDTO d_details = getDoctorDetails(event.getDoctorId(), serviceAuthToken);
        String subject = "Appointment Approved";
        String appointmentTime = event.getAppointmentDateTime() != null
                ? event.getAppointmentDateTime().format(dateTimeFormatter)
                : "N/A";
        String body = buildAppointmentApprovedHtml( event.getDoctorName(), event.getPatientName(), appointmentTime);

        emailService.sendEmailHtml(event.getPatientEmail(), subject, body);
        log.info("HTML email sent to patient {} for approval.", event.getPatientEmail());
    }

    private void notifyPatientRejection(AppointmentEvent event) {
        //UserDetailsDTO p_details = getPatientDetails(event.getPatientId(), serviceAuthToken);
        //UserDetailsDTO d_details = getDoctorDetails(event.getDoctorId(), serviceAuthToken);


        String subject = "Appointment Rejected";
        String appointmentTime = event.getAppointmentDateTime() != null
                ? event.getAppointmentDateTime().format(dateTimeFormatter)
                : "N/A";
        String body = buildAppointmentRejectedHtml( event.getDoctorName(), event.getPatientName(), appointmentTime);

        emailService.sendEmailHtml(event.getPatientEmail(), subject, body);
        log.info("HTML email sent to patient {} for rejection.", event.getPatientEmail());
    }

//    private UserDetailsDTO getDoctorDetails(int doctorId, String token) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(token);
//        HttpEntity<Void> request = new HttpEntity<>(headers);
//
//        ResponseEntity<UserDetailsDTO> response = restTemplate.exchange(
//                userServiceUrl + "/auth/id/" + doctorId,
//                HttpMethod.GET,
//                request,
//                UserDetailsDTO.class
//        );
//
//
//        if (response.getBody() != null) {
//            return response.getBody();
//        }
//        throw new RuntimeException("Doctor email not found for ID: " + doctorId);
//    }
//
//    private UserDetailsDTO getPatientDetails(int patientId, String token) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(token);
//        HttpEntity<Void> request = new HttpEntity<>(headers);
//
//        ResponseEntity<UserDetailsDTO> response = restTemplate.exchange(
//                userServiceUrl + "/auth/id/" + patientId,
//                HttpMethod.GET,
//                request,
//                UserDetailsDTO.class
//        );
//
//
//        if (response.getBody() != null) {
//            return response.getBody();
//        }
//        throw new RuntimeException("Patient email not found for ID: " + patientId);
//    }




    public String buildNewAppointmentHtmlDoc(String doctorName, String patientName, String appointmentDateTime) {
        return renderer.render("new_appointment_doc", doctorName, patientName, appointmentDateTime);
    }

    private String buildNewAppointmentHtmlPatient(String doctorName, String patientName, String appointmentDateTime) {
        return renderer.render("new_appointment_patient", doctorName, patientName, appointmentDateTime);
    }

    public String buildAppointmentApprovedHtml(String doctorName,String patientName, String appointmentDateTime) {
        return renderer.render("appointment_approved", doctorName, patientName, appointmentDateTime);
    }

    public String buildAppointmentRejectedHtml(String doctorName,String patientName, String appointmentDateTime) {
        return renderer.render("appointment_rejected", doctorName, patientName, appointmentDateTime);
    }
}
