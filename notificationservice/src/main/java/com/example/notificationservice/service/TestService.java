package com.example.notificationservice.service;

import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class TestService {

    private final KafkaTemplate<String,String> kafkaTemplate;


}

