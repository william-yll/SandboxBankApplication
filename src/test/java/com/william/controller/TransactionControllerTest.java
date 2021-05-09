package com.william.controller;

import com.william.model.request.TransactionDto;
import com.william.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    @Test
    public void testMakeTransaction() {
        TransactionDto transactionDto = TransactionDto.builder().build();

        transactionController.makeTransaction(transactionDto);

        verify(transactionService).transfer(any());
    }

    @Test
    public void testViewTransaction() {
        transactionController.viewTransaction(1L);

        verify(transactionService).balance(any());
    }

}
