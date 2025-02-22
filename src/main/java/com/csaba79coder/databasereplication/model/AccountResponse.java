package com.csaba79coder.databasereplication.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {

    private UUID id;
    private String name;
    private Double balance;
}
