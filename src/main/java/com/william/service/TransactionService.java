package com.william.service;

import com.william.model.request.TransactionDto;
import com.william.model.response.StatementResponse;

public interface TransactionService {

    void transfer(TransactionDto transactionDto);

    StatementResponse balance(Long accountId);
}
