package com.csaba79coder.databasereplication.service;

import com.csaba79coder.databasereplication.controller.exception.AlreadyExistsException;
import com.csaba79coder.databasereplication.entity.Account;
import com.csaba79coder.databasereplication.model.AccountRequest;
import com.csaba79coder.databasereplication.model.AccountResponse;
import com.csaba79coder.databasereplication.persistence.AccountRepository;
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

    private final AccountRepository accountRepository;

    public AccountResponse createAccount(AccountRequest request) {
        if (request.getName() == null || request.getName().isEmpty()) {
            String message = "Account name cannot be null or empty";
            log.error(message);
            throw new IllegalArgumentException(message);
        }
        Optional<Account> existingAccount = findAccountByName(request.getName());
        if (existingAccount.isPresent()) {
            String message = String.format("Account already exists with name: %s", request.getName());
            log.error(message);
            throw new AlreadyExistsException(message);
        }
        return mapAccountEntityToResponseModel(accountRepository.save(mapAccountRequestModelToEntity(request)));
    }

    public List<AccountResponse> findAllAccounts() {
        return accountRepository.findAll().stream()
                .map(Mapper::mapAccountEntityToResponseModel)
                .toList();
    }

    private Optional<Account> findAccountByName(String name) {
        return accountRepository.findAccountByName(name);
    }
}
