package com.DevUp.DoctorUp.DTO;

import lombok.Data;

@Data
public class JwtResponseDTO {
    private String token;
    private String role;

    private int id;

    public JwtResponseDTO() {}
    public JwtResponseDTO(String token, String role,int id) {
        this.token = token;
        this.role = role;
        this.id=id;
    }
}
