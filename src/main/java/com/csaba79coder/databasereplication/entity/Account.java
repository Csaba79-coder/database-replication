package com.csaba79coder.databasereplication.entity;

import com.csaba79coder.databasereplication.entity.base.Identifier;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account extends Identifier implements Serializable {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "balance")
    private Double balance = 0.0;
}
