package com.DevUp.DoctorUp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class DoctorUpApplication {

	public static void main(String[] args) {
		SpringApplication.run(DoctorUpApplication.class, args);
	}

}
