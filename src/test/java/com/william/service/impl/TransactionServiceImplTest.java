package com.william.service.impl;

import com.william.exception.InvalidAccountException;
import com.william.exception.InvalidAmountException;
import com.william.model.Account;
import com.william.model.Transaction;
import com.william.model.request.TransactionDto;
import com.william.model.response.StatementResponse;
import com.william.repository.AccountRepository;
import com.william.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    public void testTransfer() {
        TransactionDto transactionDto =
                TransactionDto.builder().fromAccountId(1L).toAccountId(2L).amount(BigDecimal.valueOf(1)).build();
        Account account1 = Account.builder().id(1L).balance(BigDecimal.valueOf(1)).build();
        Account account2 = Account.builder().id(2L).balance(BigDecimal.valueOf(1)).build();
        when(accountRepository.findById(any())).thenReturn(Optional.of(account1), Optional.of(account2));

        transactionService.transfer(transactionDto);

        verify(accountRepository, times(2)).findById(any());
        verify(accountRepository, times(2)).save(any());
        verify(transactionRepository, times(2)).save(any());
    }

    @Test
    public void testTransferInvalidAccount() {
        TransactionDto transactionDto =
                TransactionDto.builder().fromAccountId(1L).toAccountId(2L).amount(BigDecimal.valueOf(1)).build();
        when(accountRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(InvalidAccountException.class, () -> transactionService.transfer(transactionDto));
        verify(accountRepository, times(2)).findById(any());
        verify(accountRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    public void testTransferInvalidAmount() {
        TransactionDto transactionDto =
                TransactionDto.builder().fromAccountId(1L).toAccountId(2L).amount(BigDecimal.valueOf(2)).build();
        Account account1 = Account.builder().id(1L).balance(BigDecimal.valueOf(1)).build();
        Account account2 = Account.builder().id(2L).balance(BigDecimal.valueOf(1)).build();
        when(accountRepository.findById(any())).thenReturn(Optional.of(account1), Optional.of(account2));

        assertThrows(InvalidAmountException.class, () -> transactionService.transfer(transactionDto));

        verify(accountRepository, times(2)).findById(any());
        verify(accountRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    public void testBalance() {
        Account account = Account.builder().id(1L).balance(BigDecimal.ONE).build();
        Transaction transaction = Transaction.builder().build();
        when(accountRepository.findById(any())).thenReturn(Optional.of(account));
        when(transactionRepository.findAllByAccountId(1L)).thenReturn(Collections.singletonList(transaction));

        StatementResponse response = transactionService.balance(1L);

        StatementResponse expectedResponse = StatementResponse.builder()
                .balance(BigDecimal.valueOf(1))
                .transactionList(Collections.singletonList(transaction))
                .build();
        assertEquals(expectedResponse, response);
        verify(accountRepository).findById(any());
        verify(transactionRepository).findAllByAccountId(any());
    }

    @Test
    public void testBalanceInvalidAccount() {
        when(accountRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(InvalidAccountException.class, () -> transactionService.balance(1L));

        verify(accountRepository).findById(any());
        verify(transactionRepository, never()).findAllByAccountId(any());
    }


}
