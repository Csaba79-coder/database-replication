package com.csaba79coder.databasereplication.entity.base;

import jakarta.persistence.*;
import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

@MappedSuperclass
@Getter
public class Identifier implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
}