package com.Devup.Appointment.Service;

import com.Devup.Appointment.DTO.AppointmentRequestDTO;
import com.Devup.Appointment.DTO.AppointmentResponseDTO;

import java.util.List;

public interface AppointmentService {
    AppointmentResponseDTO createAppointment(AppointmentRequestDTO request);
    List<AppointmentResponseDTO> getAppointmentsForDoctor(int doctorId);
    List<AppointmentResponseDTO> getAppointmentsForPatient(int patientId);
    AppointmentResponseDTO approveAppointment(int appointmentId);
    AppointmentResponseDTO rejectAppointment(int appointmentId);
}
