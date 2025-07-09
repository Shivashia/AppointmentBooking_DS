package com.DevUp.DoctorUp.DTO;



import java.time.LocalDateTime;

public class AppointmentDetailsDTO {

    private int id;
    private DoctorDTO doctor;
    private PatientDTO patient;
    private LocalDateTime appointmentDateTime;
    private String status; // e.g., PENDING, APPROVED, REJECTED

    public AppointmentDetailsDTO() {
    }

    public AppointmentDetailsDTO(int id, DoctorDTO doctor, PatientDTO patient, LocalDateTime appointmentDateTime, String status) {
        this.id = id;
        this.doctor = doctor;
        this.patient = patient;
        this.appointmentDateTime = appointmentDateTime;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DoctorDTO getDoctor() {
        return doctor;
    }

    public void setDoctor(DoctorDTO doctor) {
        this.doctor = doctor;
    }

    public PatientDTO getPatient() {
        return patient;
    }

    public void setPatient(PatientDTO patient) {
        this.patient = patient;
    }

    public LocalDateTime getAppointmentDateTime() {
        return appointmentDateTime;
    }

    public void setAppointmentDateTime(LocalDateTime appointmentDateTime) {
        this.appointmentDateTime = appointmentDateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
