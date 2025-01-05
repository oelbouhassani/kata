package com.kata.service;

import com.kata.dto.AccountDto;
import com.kata.dto.TransactionDto;


import java.util.List;

public interface AccountManagementServiceI {

    AccountDto deposit(Long accountId, Double amount);
    AccountDto withdraw(Long accountId, Double amount);
    List<TransactionDto> getAccountStatement(Long accountId);
}


