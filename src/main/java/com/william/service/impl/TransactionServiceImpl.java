package com.william.service.impl;

import com.william.exception.InvalidAccountException;
import com.william.exception.InvalidAmountException;
import com.william.model.Account;
import com.william.model.Transaction;
import com.william.model.request.TransactionDto;
import com.william.model.response.StatementResponse;
import com.william.repository.AccountRepository;
import com.william.repository.TransactionRepository;
import com.william.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(final AccountRepository accountRepository,
                                  final TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public void transfer(final TransactionDto transactionDto) {
        final Optional<Account> fromAccountOptional = accountRepository.findById(transactionDto.getFromAccountId());
        final Optional<Account> toAccountOptional = accountRepository.findById(transactionDto.getToAccountId());
        if (!fromAccountOptional.isPresent() || !toAccountOptional.isPresent()) {
            throw new InvalidAccountException();
        }
        final Account fromAccount = fromAccountOptional.get();
        final Account toAccount = toAccountOptional.get();
        if (transactionDto.getAmount().compareTo(fromAccount.getBalance()) > 0) {
            throw new InvalidAmountException();
        }
        log.info("Transferring");
        transfer(fromAccount, transactionDto.getAmount().negate());
        transfer(toAccount, transactionDto.getAmount());

    }

    private void transfer(Account account, BigDecimal amount) {
        final Transaction fromTransaction = Transaction.builder()
                .account(account)
                .preTransaction(account.getBalance())
                .transactionAmount(amount)
                .postTransaction(account.getBalance().add(amount))
                .build();
        transactionRepository.save(fromTransaction);
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);
    }

    @Override
    @Lock(LockModeType.PESSIMISTIC_READ)
    public StatementResponse balance(final Long accountId) {
        final Optional<Account> accountOptional = accountRepository.findById(accountId);

        if (!accountOptional.isPresent()) {
            throw new InvalidAccountException();
        }
        final List<Transaction> transactions = transactionRepository.findAllByAccountId(accountId);
        return StatementResponse.builder()
                .balance(accountOptional.get().getBalance())
                .transactionList(transactions)
                .build();
    }
}
