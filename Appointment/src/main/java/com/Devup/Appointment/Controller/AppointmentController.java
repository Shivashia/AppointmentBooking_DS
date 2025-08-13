package com.Devup.Appointment.Controller;

import com.Devup.Appointment.DTO.*;
import com.Devup.Appointment.Entity.Appointment;
import com.Devup.Appointment.Entity.AppointmentStatus;
import com.Devup.Appointment.Repository.AppointmentRepository;

import com.Devup.Appointment.Service.AppointmentEvent;
import com.Devup.Appointment.Service.AppointmentEventPublisher;
import com.Devup.Appointment.Service.AppointmentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentRepository appointmentRepository;
    private final RestTemplate restTemplate;

    @Value("${doctor.service.url:http://DOCTOR-SERVICE}")
    private String doctorServiceUrl;

    @Value("${patient.service.url:http://PATIENT-SERVICE}")
    private String patientServiceUrl;

    @Autowired
    private AppointmentServiceImpl appointmentService;

    @Autowired
    private AppointmentEventPublisher eventPublisher;


    @PostMapping("/register")
    public ResponseEntity<Appointment> createAppointment(@RequestBody AppointmentRequestDTO dto ,@RequestHeader("Authorization") String authHeader) {
        Appointment appointment = new Appointment();
        appointment.setDoctorId(dto.getDoctorId());
        appointment.setPatientId(dto.getPatientId());
        appointment.setAppointmentDateTime(dto.getAppointmentDateTime());
        appointment.setStatus(AppointmentStatus.PENDING);
        Appointment saved = appointmentRepository.save(appointment);

        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;

        // ✅ Publish event with token
        eventPublisher.sendEvent(new AppointmentEvent(
                saved.getId(),
                saved.getDoctorId(),
                saved.getPatientId(),
                saved.getStatus().name(),
                saved.getAppointmentDateTime(),
                token
        ));

        return ResponseEntity.ok(saved);

    }



    @GetMapping
    public ResponseEntity<List<AppointmentDetailsDTO>> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();
        List<AppointmentDetailsDTO> detailsList = appointments.stream()
                .map(this::mapToDetailsDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(detailsList);
    }


    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDetailsDTO> getAppointmentById(@PathVariable int id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        return ResponseEntity.ok(mapToDetailsDTO(appointment));
    }


    @PutMapping("/{id}/approve")
    public ResponseEntity<String> approveAppointment(@PathVariable int id,@RequestHeader("Authorization") String authHeader) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        appointment.setStatus(AppointmentStatus.APPROVED);
        appointmentRepository.save(appointment);


        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
        eventPublisher.sendEvent(new AppointmentEvent(
                appointment.getId(),
                appointment.getDoctorId(),
                appointment.getPatientId(),
                appointment.getStatus().name(),
                appointment.getAppointmentDateTime(),
                token
        ));
        return ResponseEntity.ok("Appointment approved");
    }


    @PutMapping("/{id}/reject")
    public ResponseEntity<String> rejectAppointment(@PathVariable int id,@RequestHeader("Authorization") String authHeader) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        appointment.setStatus(AppointmentStatus.REJECTED);
        appointmentRepository.save(appointment);

        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
        eventPublisher.sendEvent(new AppointmentEvent(
                appointment.getId(),
                appointment.getDoctorId(),
                appointment.getPatientId(),
                appointment.getStatus().name(),
                appointment.getAppointmentDateTime(),
                token
        ));

        return ResponseEntity.ok("Appointment rejected");
    }
    private AppointmentDetailsDTO mapToDetailsDTO(Appointment appointment) {
        DoctorDTO doctor = restTemplate.getForObject(
                doctorServiceUrl + "/doctor/" + appointment.getDoctorId(), DoctorDTO.class);

        PatientDTO patient = restTemplate.getForObject(
                patientServiceUrl + "/patient/" + appointment.getPatientId(), PatientDTO.class);

        AppointmentDetailsDTO dto = new AppointmentDetailsDTO();
        dto.setId(appointment.getId());
        dto.setDoctor(doctor);
        dto.setPatient(patient);
        dto.setAppointmentDateTime(appointment.getAppointmentDateTime());
        dto.setStatus(appointment.getStatus().toString());

        return dto;
    }

    @GetMapping("/getPatientAppointment/{id}")
    private List<AppointmentResponseDTO> getPatientAppointments(@PathVariable int id,@RequestHeader("Authorization") String authHeader){
        List<AppointmentResponseDTO> patientApp=new ArrayList<>();
        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
        patientApp=appointmentService.getAppointmentsForPatient(id,token);
        return patientApp;
    }

    @GetMapping("/getDoctorAppointment/{id}")
    private List<AppointmentResponseDTO> getDoctorAppointments(@PathVariable int id,@RequestHeader("Authorization") String authHeader){
        List<AppointmentResponseDTO> doctorApp=new ArrayList<>();
        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
        doctorApp=appointmentService.getAppointmentsForDoctor(id,token);
        System.out.println(""+doctorApp.size()+ Arrays.toString(doctorApp.toArray()));
        return doctorApp;
    }
}