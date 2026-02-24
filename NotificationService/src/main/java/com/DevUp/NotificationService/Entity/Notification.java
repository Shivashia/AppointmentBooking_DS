package com.DevUp.NotificationService.Entity;

import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notification_mast", uniqueConstraints = @UniqueConstraint(
        columnNames = {"appointmentId"}
))
public class Notification {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private int appointmentId;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private LocalDateTime createdAt;


    public Notification() {
    }

    public Notification(int appointmentId, String status, LocalDateTime createdAt) {
        this.appointmentId = appointmentId;
        this.status = status;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
