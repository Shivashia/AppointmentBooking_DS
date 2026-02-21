package com.Devup.UserService.DTO;

import lombok.Data;

@Data
public class JwtResponseDTO {
    private String token;
    private String role;

    private int id;

    public JwtResponseDTO() {}
    public JwtResponseDTO(String token, String role, int id) {
        this.token = token;
        this.role = role;
        this.id=id;
    }

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
