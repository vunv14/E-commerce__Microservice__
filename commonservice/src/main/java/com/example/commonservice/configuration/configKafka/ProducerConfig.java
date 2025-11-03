package com.example.commonservice.configuration.configKafka;


import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;
import static org.apache.kafka.clients.CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.ACKS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.BATCH_SIZE_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.BUFFER_MEMORY_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.COMPRESSION_TYPE_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.LINGER_MS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.RETRIES_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;

@Configuration
@AllArgsConstructor
public class ProducerConfig {

    private final KafkaProperties kafkaProperties;

    @Bean
    public ProducerFactory<String, String> kafkaProducerFactory(){
        Map<String, Object> props = new HashMap<>();
        props.put(BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers()); // Đặt địa chỉ Kafka broker từ KafkaProperties.
        props.put(VALUE_SERIALIZER_CLASS_CONFIG,kafkaProperties.getValue());
        props.put(KEY_SERIALIZER_CLASS_CONFIG, kafkaProperties.getKey()); // Chỉ định class dùng để serialize key của tin nhắn thành byte.
        props.put(ACKS_CONFIG,kafkaProperties.getAcks());
        props.put(RETRIES_CONFIG,kafkaProperties.getRetries());
        props.put(BATCH_SIZE_CONFIG,kafkaProperties.getBatchSize());
        props.put(LINGER_MS_CONFIG,1); // Chờ 1s thu thập bản ghi
        props.put(COMPRESSION_TYPE_CONFIG,kafkaProperties.getCompressionType());
        props.put(ENABLE_IDEMPOTENCE_CONFIG,true);
        props.put(BUFFER_MEMORY_CONFIG,33554432);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(kafkaProducerFactory());
    }
}
