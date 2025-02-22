package com.csaba79coder.databasereplication.controller;

import com.csaba79coder.databasereplication.model.AccountRequest;
import com.csaba79coder.databasereplication.model.AccountResponse;
import com.csaba79coder.databasereplication.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/accounts")
    public ResponseEntity<AccountResponse> createAccount(@RequestBody AccountRequest request) {
        return ResponseEntity.status(201).body(accountService.createAccount(request));
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<AccountResponse>> renderAllAccounts() {
        return ResponseEntity.status(200).body(accountService.findAllAccounts());
    }
}
