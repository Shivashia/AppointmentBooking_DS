package com.DevUp.DoctorUp.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentBookingDTO {
    private int doctorId;
    private int patientId;
    private LocalDateTime appointmentDateTime;
}
