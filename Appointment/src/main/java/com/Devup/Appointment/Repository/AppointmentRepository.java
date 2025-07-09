package com.Devup.Appointment.Repository;

import com.Devup.Appointment.Entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    List<Appointment> findByDoctorId(int doctorId);
    List<Appointment> findByPatientId(int patientId);
}