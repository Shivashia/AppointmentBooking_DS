package com.DevUp.NotificationService.Repository;

import com.DevUp.NotificationService.Entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;
//import java.util.UUID;

public interface NotificationRepository  extends JpaRepository<Notification, UUID> {

    Optional<Notification> findByAppointmentIdAndStatus(int appointmentId,
                                                       String status);

    List<Notification> findAllByAppointmentIdAndStatus(int appointmentId,
                                                      String status);

    List<Notification> findTop10ByStatus(String status);

    Notification findByAppointmentId(int appointmentId);

    Boolean existsByAppointmentId(int appointmentId);
}
