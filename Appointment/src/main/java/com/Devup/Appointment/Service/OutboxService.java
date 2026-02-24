package com.Devup.Appointment.Service;

import com.Devup.Appointment.Entity.OutboxEvent;
import com.Devup.Appointment.Repository.OutboxEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Service
public class OutboxService {

    @Autowired
    private OutboxEventRepository outboxEventRepository;

    @Autowired
    private AppointmentEventPublisher eventPublisher;

    @Autowired
    private AppointmentServiceImpl appointmentService;


    private String userServiceUrl="http://USER-SERVICE";

    @Autowired
    private RestTemplate restTemplate;

    @Transactional
    public List<OutboxEvent> lockNewEvents(){
        List<OutboxEvent> newEvents= outboxEventRepository.findTop10ByStatus("NEW");


        for(OutboxEvent event: newEvents){
            event.setStatus("PROCESSING");
            System.out.println("Event print "+event.toString());
        }

        return newEvents;
    }

    @Transactional
    public void markProcessed(UUID id) {
        outboxEventRepository.updateStatus(id, "PROCESSED");
    }

    @Transactional
    public void markFailed(UUID id) {
        outboxEventRepository.updateStatus(id, "FAILED");
    }
}
