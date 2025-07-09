package com.Devup.Appointment.DTO;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequestDTO {
    private int patientId;
    private int doctorId;
    private LocalDateTime appointmentDateTime;
}
