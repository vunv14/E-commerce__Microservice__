package com.example.commonservice.configKafka;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "common.kafka")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class KafkaProperties {

    private String bootstrapServers;
    private String key;
    private String value;
    private String acks;
    private String retries;
    private String batchSize;
    private String bufferMemory;
    private String compressionType;

    private String groupId;
    private String autoOffsetReset;
    private String enableAutoCommit;

}
