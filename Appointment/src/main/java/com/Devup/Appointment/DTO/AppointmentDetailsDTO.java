package com.Devup.Appointment.DTO;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AppointmentDetailsDTO {
    private int id;
    private DoctorDTO doctor;
    private PatientDTO patient;
    private LocalDateTime appointmentDateTime;
    private String status; // e.g., PENDING, APPROVED, REJECTED
}