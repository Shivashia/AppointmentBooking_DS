package com.Devup.Appointment.Repository;

import com.Devup.Appointment.Entity.Appointment;
import com.Devup.Appointment.Entity.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OutboxEventRepository  extends JpaRepository<OutboxEvent, UUID> {

    OutboxEvent findByAppointmentId(UUID appointmentId);

    OutboxEventRepository findByStatus(String status);
}
