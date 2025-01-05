package com.kata.controller;

import com.kata.dto.AccountDto;
import com.kata.dto.TransactionDto;
import com.kata.service.AccountManagementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing bank accounts.
 * Provides endpoints for deposit, withdrawal, and retrieving account statements.
 */
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountManagementService accountManagementService;

    /**
     * Constructor for AccountController.
     *
     * @param accountManagementService The service responsible for managing account operations.
     */
    public AccountController(AccountManagementService accountManagementService) {
        this.accountManagementService = accountManagementService;
    }

    /**
     * Endpoint to deposit a specified amount into an account.
     *
     * @param accountId The ID of the account to deposit into.
     * @param amount The amount to deposit into the account.
     * @return The updated Account after deposit.
     */
    @PostMapping("/{accountId}/deposit")
    public ResponseEntity<AccountDto> deposit(@PathVariable Long accountId, @RequestParam Double amount) {
        return ResponseEntity.ok(accountManagementService.deposit(accountId, amount));
    }

    /**
     * Endpoint to withdraw a specified amount from an account.
     *
     * @param accountId The ID of the account to withdraw from.
     * @param amount The amount to withdraw from the account.
     * @return The updated Account after withdrawal.
     */
    @PostMapping("/{accountId}/withdraw")
    public ResponseEntity<AccountDto> withdraw(@PathVariable Long accountId, @RequestParam Double amount) {
        return ResponseEntity.ok(accountManagementService.withdraw(accountId, amount));
    }

    /**
     * Endpoint to retrieve the statement of a specific account.
     *
     * @param accountId The ID of the account for which to retrieve the statement.
     * @return A list of Transactions associated with the account.
     */
    @GetMapping("/{accountId}/statement")
    public ResponseEntity<List<TransactionDto>> getStatement(@PathVariable Long accountId) {
        return ResponseEntity.ok(accountManagementService.getAccountStatement(accountId));
    }
}
