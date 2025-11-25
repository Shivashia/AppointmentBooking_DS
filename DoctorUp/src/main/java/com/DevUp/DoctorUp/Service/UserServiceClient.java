package com.DevUp.DoctorUp.Service;

import com.DevUp.DoctorUp.DTO.UserRegistrationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserServiceClient {
//    @Value("${user.service.url:http://USER-SERVICE}")
    private String userServiceUrl="http://USER-SERVICE";

    @Autowired
    private RestTemplate restTemplate;

    public void registerUser(UserRegistrationDTO dto) {
        System.out.println("Sent to User Service");
        restTemplate.postForObject(
                userServiceUrl + "/auth/register",
                dto,
                String.class
        );
    }
}
