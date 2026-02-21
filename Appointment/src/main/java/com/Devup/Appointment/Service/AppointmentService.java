package com.Devup.Appointment.Service;

import com.Devup.Appointment.DTO.AppointmentRequestDTO;
import com.Devup.Appointment.DTO.AppointmentResponseDTO;

import java.util.List;

public interface AppointmentService {
    AppointmentResponseDTO createAppointment(AppointmentRequestDTO request, String token);
    List<AppointmentResponseDTO> getAppointmentsForDoctor(int doctorId,String token);
    List<AppointmentResponseDTO> getAppointmentsForPatient(int patientId,String token);
    AppointmentResponseDTO approveAppointment(int appointmentId,String token);
    AppointmentResponseDTO rejectAppointment(int appointmentId,String token);
}
