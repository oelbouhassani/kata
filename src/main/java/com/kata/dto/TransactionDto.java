package com.kata.dto;

import com.kata.enumuration.TransactionType;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDto {
    private LocalDateTime date;
    private Double amount;
    private Double balance;
    private TransactionType transactionType;
    private AccountDto account;
}