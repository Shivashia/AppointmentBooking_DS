package com.DevUp.NotificationService.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MailDTO {

    private String email_p;
    private String email_d;
    private LocalDateTime appointmentDateTime;
}
