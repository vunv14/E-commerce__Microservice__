package com.example.commonservice.configuration.configKafka.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class Consumer {

    private String groupId;
    private String autoOffsetReset;
    private String enableAutoCommit;

    private Map<String, String> properties = new HashMap<>();

}
