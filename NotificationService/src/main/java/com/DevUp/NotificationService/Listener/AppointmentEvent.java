package com.DevUp.NotificationService.Listener;

import com.DevUp.NotificationService.DTO.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentEvent implements Serializable {
//    private int id;
    private String doctorName;
    private String  patientName;
    private String doctorEmail;
    private String patientEmail;
    private AppointmentStatus status;  // PENDING, APPROVED, REJECTED
    private LocalDateTime appointmentDateTime;
    private int appointmentId;
    //private String token;


}
