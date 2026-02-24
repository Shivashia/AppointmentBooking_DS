package com.Devup.Appointment.Service;


import com.Devup.Appointment.DTO.AppointmentEvent;
import com.Devup.Appointment.DTO.PatientPublicDTO;
import com.Devup.Appointment.Entity.OutboxEvent;
import com.Devup.Appointment.Repository.OutboxEventRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class NotificationScheduler {


    @Autowired
    private OutboxEventRepository outboxEventRepository;

    @Autowired
    private AppointmentEventPublisher eventPublisher;

    @Autowired
    private AppointmentServiceImpl appointmentService;

    @Autowired
    private OutboxService outboxService;


    private String userServiceUrl="http://USER-SERVICE";

    @Autowired
    private  RestTemplate restTemplate;




    @Scheduled(fixedDelay = 2000)
    public void scheduleNotifications() throws JsonProcessingException {

        System.out.println("scheduling Notifications "+ LocalDateTime.now());

        List<OutboxEvent> currentEvents= outboxService.lockNewEvents();


        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        for (OutboxEvent event: currentEvents){

            try {
                String paylod=event.getPayload();
                AppointmentEvent dto =
                        objectMapper.readValue(paylod, AppointmentEvent.class);
                eventPublisher.sendEvent(dto);
                outboxService.markProcessed(event.getId());
            }catch (Exception e){
                System.out.println(e);
                outboxService.markFailed(event.getId());
            }

        }
    }



}
