package com.example.notificationservice.entity.base;

import com.example.commonservice.contants.EntityStatus;
import com.example.commonservice.listener.CustomerPrimaryEntity;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@EntityListeners(CustomerPrimaryEntity.class)
public abstract class PrimaryEntity extends AuditEntity implements IsIdentified{

    @Id
    @Column
    private String id;

    @Column(name = "status")
    @Enumerated(EnumType.ORDINAL)
    private EntityStatus status;

}
