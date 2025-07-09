package com.Devup.Appointment.DTO;

import com.Devup.Appointment.Entity.AppointmentStatus;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponseDTO {
    private int id;
    private PatientPublicDTO patient;
    private DoctorPublicDTO doctor;
    private LocalDateTime appointmentDateTime;
    private AppointmentStatus status;
}