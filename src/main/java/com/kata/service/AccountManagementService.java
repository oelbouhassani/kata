package com.kata.service;

import com.kata.dto.AccountDto;
import com.kata.dto.TransactionDto;
import com.kata.mapper.AccountMapper;
import com.kata.mapper.TransactionMapper;
import com.kata.model.Account;
import com.kata.model.Transaction;
import com.kata.enumuration.TransactionType;
import com.kata.repository.AccountRepository;
import com.kata.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service class for managing account-related operations such as deposits, withdrawals, and account statements.
 * This class handles the business logic for modifying account balances and recording transactions.
 */
@Service
@Transactional
public class AccountManagementService implements AccountManagementServiceI {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final AccountMapper accountMapper;
    private final TransactionMapper transactionMapper;


    /**
     * Constructor for AccountManagementService.
     *
     * @param accountRepository     The repository for accessing account data.
     * @param transactionRepository The repository for accessing transaction data.
     * @param accountMapper
     * @param transactionMapper
     */
    public AccountManagementService(AccountRepository accountRepository, TransactionRepository transactionRepository, AccountMapper accountMapper, TransactionMapper transactionMapper) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.accountMapper = accountMapper;
        this.transactionMapper = transactionMapper;
    }

    /**
     * Deposits a specified amount into the account.
     *
     * @param accountId The ID of the account to deposit into.
     * @param amount The amount to deposit.
     * @return The updated Account after the deposit.
     * @throws RuntimeException if the account cannot be found.
     */
    public AccountDto deposit(Long accountId, Double amount) {
        Account account = getAccount(accountId);

        // Update the account balance and create a transaction record
        account.setBalance(account.getBalance() + amount);

        Transaction transaction = Transaction.builder()
                .date(LocalDateTime.now())
                .amount(amount)
                .balance(account.getBalance())
                .account(account)
                .transactionType(TransactionType.DEPOSIT)
                .build();
        transactionRepository.save(transaction);

        return accountMapper.toAccountDto(accountRepository.save(account));
    }

    /**
     * Withdraws a specified amount from the account.
     *
     * @param accountId The ID of the account to withdraw from.
     * @param amount The amount to withdraw.
     * @return The updated Account after the withdrawal.
     * @throws RuntimeException if the account cannot be found or if there are insufficient funds.
     */
    public AccountDto withdraw(Long accountId, Double amount) {
        Account account = getAccount(accountId);

        if (account.getBalance() < amount) {
            throw new RuntimeException("Insufficient funds");
        }

        // Update the account balance and create a transaction record
        account.setBalance(account.getBalance() - amount);

        Transaction transaction = Transaction.builder()
                .date(LocalDateTime.now())
                .amount(amount)
                .balance(account.getBalance())
                .account(account)
                .transactionType(TransactionType.WITHDRAW)
                .build();

        transactionRepository.save(transaction);

        return accountMapper.toAccountDto(accountRepository.save(account));
    }

    /**
     * Retrieves the statement (list of transactions) for a specific account.
     *
     * @param accountId The ID of the account for which to retrieve the statement.
     * @return A list of transactions associated with the account.
     * @throws RuntimeException if the account cannot be found.
     */
    public List<TransactionDto> getAccountStatement(Long accountId) {
        Account account = getAccount(accountId);
        return transactionMapper.toTransactionDtos(account.getTransactions());
    }

    /**
     * Retrieves an account by its ID. Throws an exception if the account is not found.
     *
     * @param accountId The ID of the account to retrieve.
     * @return The Account object associated with the provided ID.
     * @throws RuntimeException if the account cannot be found.
     */
    private Account getAccount(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }
}
