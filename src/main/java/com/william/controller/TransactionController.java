package com.william.controller;

import com.william.model.request.TransactionDto;
import com.william.model.response.StatementResponse;
import com.william.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping(value = "/transfer", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity makeTransaction(@RequestBody final TransactionDto transactionDto) {
        log.info("Sending {} from account: {} to account: {}", transactionDto.getAmount(),
                transactionDto.getFromAccountId(),
                transactionDto.getToAccountId());
        transactionService.transfer(transactionDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/balance", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StatementResponse> viewTransaction(@RequestParam("accountId") final Long accountId) {
        log.info("Getting account balance");
        final StatementResponse statementResponse = transactionService.balance(accountId);
        return ResponseEntity.ok(statementResponse);
    }
}
