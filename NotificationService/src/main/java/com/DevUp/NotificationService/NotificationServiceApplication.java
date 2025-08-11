package com.DevUp.NotificationService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;

@SpringBootApplication
@EnableEurekaClient
@EnableJms
public class NotificationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationServiceApplication.class, args);
	}

}
