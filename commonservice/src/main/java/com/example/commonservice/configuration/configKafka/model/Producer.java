package com.example.commonservice.configuration.configKafka.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;


@Getter
@Setter

public class Producer {

    private String key;
    private String value;
    private String acks;
    private String retries;
    private String batchSize;
    private String bufferMemory;
    private String compressionType;

    private Map<String, String> properties = new HashMap<>();

}
