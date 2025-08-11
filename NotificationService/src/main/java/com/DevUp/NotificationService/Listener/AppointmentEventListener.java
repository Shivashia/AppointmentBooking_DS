package com.DevUp.NotificationService.Listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AppointmentEventListener {

    @Autowired
    private ObjectMapper objectMapper;

    @JmsListener(destination = "appointment.queue")
    public void handleAppointmentEvent(String message) {
        try {
            AppointmentEvent event = objectMapper.readValue(message, AppointmentEvent.class);
            log.info("Received Appointment Event: {}", event);

            switch (event.getStatus()) {
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

    private void notifyDoctor(AppointmentEvent event) {
        log.info("Notifying Doctor {} about new appointment with Patient {}",
                event.getDoctorId(), event.getPatientId());
        // TODO: send email or push notification to doctor
    }

    private void notifyPatientApproval(AppointmentEvent event) {
        log.info("Notifying Patient {}: Appointment {} approved",
                event.getPatientId(), event.getAppointmentId());
        // TODO: send email or push notification to patient
    }

    private void notifyPatientRejection(AppointmentEvent event) {
        log.info("Notifying Patient {}: Appointment {} rejected",
                event.getPatientId(), event.getAppointmentId());
        // TODO: send email or push notification to patient
    }
}
