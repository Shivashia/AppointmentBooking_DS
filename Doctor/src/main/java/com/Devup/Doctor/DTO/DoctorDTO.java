package com.Devup.Doctor.DTO;

import lombok.*;


public class DoctorDTO {
    private int id;
    private String name;
    private String specialization;
    private String email;

    public DoctorDTO() {
    }

//    public DoctorDTO(int id, String name, String specialization, String email) {
//        this.id = id;
//        this.name = name;
//        this.specialization = specialization;
//        this.email = email;
//    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "DoctorDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", specialization='" + specialization + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
