package com.Devup.Appointment.Entity;




import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "outbox_event")
public class OutboxEvent {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String aggregateType;

    @Column(nullable = false)
    private int appointmentId;

    @Column(nullable = false)
    private String eventType;

    @Column(columnDefinition = "CLOB", nullable = false)
    private String payload;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public OutboxEvent() {}

    public OutboxEvent(String aggregateType,
                       int appointmentId,
                       String eventType,
                       String payload,
                       String status) {
        this.aggregateType = aggregateType;
        this.appointmentId = appointmentId;
        this.eventType = eventType;
        this.payload = payload;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    // getters & setters


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAggregateType() {
        return aggregateType;
    }

    public void setAggregateType(String aggregateType) {
        this.aggregateType = aggregateType;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
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

    @Override
    public String toString() {
        return "OutboxEvent{" +
                "id=" + id +
                ", aggregateType='" + aggregateType + '\'' +
                ", appointmentId=" + appointmentId +
                ", eventType='" + eventType + '\'' +
                ", payload='" + payload + '\'' +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
