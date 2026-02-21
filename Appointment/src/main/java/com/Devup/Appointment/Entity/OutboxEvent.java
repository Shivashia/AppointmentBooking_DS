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
}
