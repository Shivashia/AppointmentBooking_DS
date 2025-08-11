package com.Devup.Appointment.Service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class AppointmentEventPublisher {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void sendEvent(AppointmentEvent event) {
        try {
            String message = objectMapper.writeValueAsString(event);
            jmsTemplate.convertAndSend("appointment.queue", message);
            System.out.println("Sent event: " + event);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing event", e);
        }
    }
}
