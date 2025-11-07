package com.example.commonservice;

import com.example.commonservice.configuration.configKafka.KafkaProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableConfigurationProperties(KafkaProperties.class)
@EnableCaching
public class CommonserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommonserviceApplication.class, args);
    }

}

