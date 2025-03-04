package com.csaba79coder.databasereplication.service;

import com.csaba79coder.databasereplication.config.RoutingDataSource;
import com.csaba79coder.databasereplication.controller.exception.AlreadyExistsException;
import com.csaba79coder.databasereplication.entity.Account;
import com.csaba79coder.databasereplication.model.AccountRequest;
import com.csaba79coder.databasereplication.model.AccountResponse;
import com.csaba79coder.databasereplication.persistence.master.AccountMasterRepository;
import com.csaba79coder.databasereplication.persistence.replica.AccountReplicaRepository;
import com.csaba79coder.databasereplication.util.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.csaba79coder.databasereplication.util.Mapper.mapAccountEntityToResponseModel;
import static com.csaba79coder.databasereplication.util.Mapper.mapAccountRequestModelToEntity;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {

    private final AccountMasterRepository accountMasterRepository; // Írási műveletek
    private final AccountReplicaRepository accountReplicaRepository; // Olvasási műveletek
    private final RoutingDataSource routingDataSource; // Routing adatforrás

    public AccountResponse createAccount(AccountRequest request) {
        // Állítsuk be a művelet típusát írásra
        routingDataSource.setCurrentOperationType("WRITE");

        if (request.getName() == null || request.getName().isEmpty()) {
            String message = "Account name cannot be null or empty";
            log.error(message);
            throw new IllegalArgumentException(message);
        }

        Optional<Account> existingAccount = findAccountByName(request.getName(), "master"); // Írási művelet
        if (existingAccount.isPresent()) {
            String message = String.format("Account already exists with name: %s", request.getName());
            log.error(message);
            throw new AlreadyExistsException(message);
        }

        Account savedAccount = accountMasterRepository.save(mapAccountRequestModelToEntity(request)); // Írás
        return mapAccountEntityToResponseModel(savedAccount);
    }

    public List<AccountResponse> findAllAccounts() {
        routingDataSource.setCurrentOperationType("SELECT"); // Ezt elsőként állítsd be!
        log.info("Operation type set to SELECT"); // DEBUG: Ezt nézd meg a logban!

        List<AccountResponse> results = accountReplicaRepository.findAll().stream()
                .map(Mapper::mapAccountEntityToResponseModel)
                .toList();

        log.info("Returning {} accounts from replica", results.size()); // DEBUG: Itt nézd meg, hogy valóban olvasott-e!
        return results;
    }

    private Optional<Account> findAccountByName(String name, String type) {
        if ("master".equals(type)) {
            return accountMasterRepository.findAccountByName(name); // Írási adatbázis
        } else {
            return accountReplicaRepository.findAccountByName(name); // Olvasási adatbázis
        }
    }
}
