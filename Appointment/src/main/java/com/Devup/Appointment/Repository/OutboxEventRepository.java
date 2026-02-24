package com.Devup.Appointment.Repository;

import com.Devup.Appointment.Entity.Appointment;
import com.Devup.Appointment.Entity.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OutboxEventRepository  extends JpaRepository<OutboxEvent, UUID> {

    OutboxEvent findByAppointmentId(UUID appointmentId);

    List<OutboxEvent> findByStatus(String status);

    List<OutboxEvent> findTop10ByStatus(String aNew);

    @Modifying
    @Query("UPDATE OutboxEvent e SET e.status = :status WHERE e.id = :id")
    void updateStatus(@Param("id") UUID id,
                      @Param("status") String status);

}
