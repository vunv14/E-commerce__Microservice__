package com.example.notificationservice.util;


import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String,String> kafkaTemplate;

    public  void sendMessage(String message){
        kafkaTemplate.send("demo", message);
    }

}
