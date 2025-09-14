package com.example.commonservice.listener;


import com.example.commonservice.common.enums.EntityStatus;
import com.example.commonservice.entities.base.PrimaryEntity;
import jakarta.persistence.PrePersist;
import java.util.UUID;

public class PrimaryEntityListener {

    @PrePersist
    private void onCreate(PrimaryEntity entity) {
        if (entity.getId() == null) {
            entity.setId(UUID.randomUUID().toString());
        }
        if (entity.getStatus() == null) {
            entity.setStatus(EntityStatus.ACTIVE);
        }
    }

}