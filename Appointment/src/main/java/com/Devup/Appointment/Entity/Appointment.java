package com.Devup.Appointment.Entity;

import javax.persistence.*;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Builder
@Table(name = "user_appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int doctorId;
    private int patientId;

    private LocalDateTime appointmentDateTime;



    @Enumerated(EnumType.STRING)
    private AppointmentStatus status; // PENDING, APPROVED, REJECTED

    public Appointment() {
    }

    public Appointment(int id, int doctorId, int patientId, LocalDateTime appointmentDateTime, AppointmentStatus status) {
        this.id = id;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.appointmentDateTime = appointmentDateTime;
        this.status = status;
    }


//    public Appointment(int id, int doctorId, int patientId, LocalDateTime appointmentDateTime, UUID appointmentID, AppointmentStatus status) {
//        this.id = id;
//        this.doctorId = doctorId;
//        this.patientId = patientId;
//        this.appointmentDateTime = appointmentDateTime;
//        this.appointmentID = appointmentID;
//        this.status = status;
//    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public LocalDateTime getAppointmentDateTime() {
        return appointmentDateTime;
    }

    public void setAppointmentDateTime(LocalDateTime appointmentDateTime) {
        this.appointmentDateTime = appointmentDateTime;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

//    public UUID getAppointmentID() {
//        return appointmentID;
//    }
//
//    public void setAppointmentID(UUID appointmentID) {
//        this.appointmentID = appointmentID;
//    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", doctorId=" + doctorId +
                ", patientId=" + patientId +
                ", appointmentDateTime=" + appointmentDateTime +
//                ", appointmentID=" + appointmentID +
                ", status=" + status +
                '}';
    }
}
