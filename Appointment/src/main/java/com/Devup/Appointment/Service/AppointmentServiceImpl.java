package com.Devup.Appointment.Service;

import com.Devup.Appointment.Controller.AppointmentController;
import com.Devup.Appointment.DTO.*;
import com.Devup.Appointment.Entity.Appointment;
import com.Devup.Appointment.Entity.AppointmentStatus;
import com.Devup.Appointment.Entity.OutboxEvent;
import com.Devup.Appointment.Repository.AppointmentRepository;

import com.Devup.Appointment.Repository.OutboxEventRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl {

    private final AppointmentRepository repository;

    private final RestTemplate restTemplate;

    private final OutboxEventRepository outboxEventRepository;

    @Value("${doctor.service.url:http://DOCTOR-SERVICE}")
    private String doctorServiceUrl;



    @Value("${patient.service.url:http://PATIENT-SERVICE}")
    private String patientServiceUrl;

    private String userServiceUrl="http://USER-SERVICE";

    private static final String THIS_COMPONENT_NAME= AppointmentServiceImpl.class.getName();

//    @Override
    @Transactional
    public Appointment createAppointment(Appointment request) throws JsonProcessingException {
        Appointment appointment = Appointment.builder()
                .doctorId(request.getDoctorId())
                .patientId(request.getPatientId())
                .appointmentDateTime(request.getAppointmentDateTime())
                .status(AppointmentStatus.PENDING)
                .build();


        Appointment saved = repository.save(appointment);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(appointment);

        System.out.println("Appointment Controller "+ saved);



        OutboxEvent event= new OutboxEvent(THIS_COMPONENT_NAME,saved.getId(),"CREATED",json,"NOT_PUBLISHED");
        outboxEventRepository.save(event);
        return saved;
    }

//    @Override
    public List<AppointmentResponseDTO> getAppointmentsForDoctor(int doctorId,String token) {
        return repository.findByDoctorId(doctorId).stream()
                .map(appointment -> mapToDTO(appointment, token))
                .collect(Collectors.toList());
    }

//    @Override
    public List<AppointmentResponseDTO> getAppointmentsForPatient(int id,String token) {
        System.out.println("Appointment Service call for patientID "+id);
        return repository.findByPatientId(id).stream()
                .map(appointment -> mapToDTO(appointment, token))
                .collect(Collectors.toList());
    }

//    @Override
    public AppointmentResponseDTO approveAppointment(int appointmentId,String token) {
        Appointment appointment = repository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        appointment.setStatus(AppointmentStatus.APPROVED);
        return mapToDTO(repository.save(appointment),token);
    }

//    @Override
    public AppointmentResponseDTO rejectAppointment(int appointmentId,String token) {
        Appointment appointment = repository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        appointment.setStatus(AppointmentStatus.REJECTED);
        return mapToDTO(repository.save(appointment),token);
    }

    private AppointmentResponseDTO mapToDTO(Appointment appointment, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        System.out.println(appointment.toString());
//        ResponseEntity<PatientDTO> patientResponse = restTemplate.exchange(
//                patientServiceUrl + "/patient/" + appointment.getPatientId(),
//                HttpMethod.GET,
//                request,
//                PatientDTO.class);
        ResponseEntity<UserDetailsDTO> response = restTemplate.exchange(
                userServiceUrl + "/auth/id/" + appointment.getPatientId(),
                HttpMethod.GET,
                request,
                UserDetailsDTO.class
        );
        //PatientDTO patientFull = response.getBody();
        PatientPublicDTO patientDTO = new PatientPublicDTO(response.getBody().getId(), response.getBody().getName(), response.getBody().getEmail());

//        ResponseEntity<DoctorDTO> doctorResponse = restTemplate.exchange(
//                doctorServiceUrl + "/doctor/" + appointment.getDoctorId(),
//                HttpMethod.GET,
//                request,
//                DoctorDTO.class);
        ResponseEntity<UserDetailsDTO> response_doc = restTemplate.exchange(
                userServiceUrl + "/auth/id/" + appointment.getDoctorId(),
                HttpMethod.GET,
                request,
                UserDetailsDTO.class
        );
        //DoctorDTO doctorFull = doctorResponse.getBody();
        DoctorPublicDTO doctorDTO = new DoctorPublicDTO(response_doc.getBody().getId(), response_doc.getBody().getName(), response_doc.getBody().getEmail(), response_doc.getBody().getSpecialization());

        return new AppointmentResponseDTO(
                appointment.getId(),
                patientDTO,
                doctorDTO,
                appointment.getAppointmentDateTime(),
                appointment.getStatus()
        );
    }

}