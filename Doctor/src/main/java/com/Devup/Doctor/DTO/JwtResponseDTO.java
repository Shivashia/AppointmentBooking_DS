package com.Devup.Doctor.DTO;

public class JwtResponseDTO {
    private String token;
    private String role;

    // Constructor
    public JwtResponseDTO(String token, String role) {
        this.token = token;
        this.role = role;
    }

    public JwtResponseDTO() {}

    // Getters and setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}