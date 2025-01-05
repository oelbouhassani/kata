package com.kata.mapper;

import com.kata.dto.AccountDto;
import com.kata.model.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountDto toAccountDto(Account account);
}
