package com.kata.mapper;

import com.kata.dto.TransactionDto;
import com.kata.model.Transaction;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionDto toTransactionDto(Transaction transaction);
    List<TransactionDto> toTransactionDtos(List<Transaction> transactions);
}
