package com.DevUp.NotificationService.DTO;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDTO {
    private int id;
    private String name;
    private String email;
    private String specialization;
    private String password;
}
