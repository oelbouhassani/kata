package com.kata.model;

import com.kata.enumuration.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime date;
    private Double amount;
    private Double balance;
    private TransactionType transactionType;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}