package com.kata.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
public class AccountDto {
    private Double balance;
    private String accountNumber;
}