package com.DevUp.DoctorUp.DTO;

import lombok.Data;

@Data
public class JwtResponseDTO {
    private String token;
    private String role;

    public JwtResponseDTO() {}
    public JwtResponseDTO(String token, String role) {
        this.token = token;
        this.role = role;
    }
}
