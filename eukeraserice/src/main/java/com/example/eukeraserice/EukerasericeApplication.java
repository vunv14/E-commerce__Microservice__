package com.example.eukeraserice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EukerasericeApplication {

	public static void main(String[] args) {
		SpringApplication.run(EukerasericeApplication.class, args);
	}

}
