package com.DevUp.NotificationService.DTO;

public class UserDetailsDTO {

    private String name;
    private String email;
    private String phone;
    private String specialization;
    private int age;
    private String gender;
    private String role;

    private int id;

    public UserDetailsDTO() {
    }

    public UserDetailsDTO(int id, String name, String email, String phone, String specialization, int age, String gender, String role) {
        this.id=id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.specialization = specialization;
        this.age = age;
        this.gender = gender;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
