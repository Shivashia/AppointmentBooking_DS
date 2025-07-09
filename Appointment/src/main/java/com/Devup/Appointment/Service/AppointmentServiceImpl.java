package com.Devup.Appointment.Service;

import com.Devup.Appointment.DTO.*;
import com.Devup.Appointment.Entity.Appointment;
import com.Devup.Appointment.Entity.AppointmentStatus;
import com.Devup.Appointment.Repository.AppointmentRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl {

    private final AppointmentRepository repository;

    private final RestTemplate restTemplate;

    @Value("${doctor.service.url:http://DOCTOR-SERVICE}")
    private String doctorServiceUrl;

    @Value("${patient.service.url:http://PATIENT-SERVICE}")
    private String patientServiceUrl;

//    @Override
    public AppointmentResponseDTO createAppointment(AppointmentRequestDTO request,String token) {
        Appointment appointment = Appointment.builder()
                .doctorId(request.getDoctorId())
                .patientId(request.getPatientId())
                .appointmentDateTime(request.getAppointmentDateTime())
                .status(AppointmentStatus.PENDING)
                .build();

        Appointment saved = repository.save(appointment);
        return mapToDTO(saved,token);
    }

//    @Override
    public List<AppointmentResponseDTO> getAppointmentsForDoctor(int doctorId,String token) {
        return repository.findByDoctorId(doctorId).stream()
                .map(appointment -> mapToDTO(appointment, token))
                .collect(Collectors.toList());
    }

//    @Override
    public List<AppointmentResponseDTO> getAppointmentsForPatient(int patientId,String token) {
        return repository.findByPatientId(patientId).stream()
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

        ResponseEntity<PatientDTO> patientResponse = restTemplate.exchange(
                patientServiceUrl + "/patient/" + appointment.getPatientId(),
                HttpMethod.GET,
                request,
                PatientDTO.class);
        PatientDTO patientFull = patientResponse.getBody();
        PatientPublicDTO patientDTO = new PatientPublicDTO(patientFull.getId(), patientFull.getName(), patientFull.getEmail());

        ResponseEntity<DoctorDTO> doctorResponse = restTemplate.exchange(
                doctorServiceUrl + "/doctor/" + appointment.getDoctorId(),
                HttpMethod.GET,
                request,
                DoctorDTO.class);
        DoctorDTO doctorFull = doctorResponse.getBody();
        DoctorPublicDTO doctorDTO = new DoctorPublicDTO(doctorFull.getId(), doctorFull.getName(), doctorFull.getEmail(), doctorFull.getSpecialization());

        return new AppointmentResponseDTO(
                appointment.getId(),
                patientDTO,
                doctorDTO,
                appointment.getAppointmentDateTime(),
                appointment.getStatus()
        );
    }

}