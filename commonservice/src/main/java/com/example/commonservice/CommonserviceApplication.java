package com.example.commonservice;

import com.example.commonservice.configuration.configKafka.KafkaProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(KafkaProperties.class)
public class CommonserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommonserviceApplication.class, args);
	}

}
