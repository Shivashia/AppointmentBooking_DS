package com.Devup.Appointment.Service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentEvent implements Serializable {
    private int appointmentId;
    private int doctorId;
    private int patientId;
    private String status;  // PENDING, APPROVED, REJECTED
    private LocalDateTime appointmentDateTime;
}