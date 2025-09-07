package com.example.notificationservice.listener;

import com.example.commonservice.base.PrimaryEntity;
import com.example.commonservice.contants.EntityStatus;
import jakarta.persistence.PrePersist;

import java.util.UUID;

public class CustomerPrimaryEntity {


    @PrePersist
    private void onCreate(PrimaryEntity entity) {
        entity.setId(UUID.randomUUID().toString());
        entity.setStatus(EntityStatus.ACTIVE);
    }

}
