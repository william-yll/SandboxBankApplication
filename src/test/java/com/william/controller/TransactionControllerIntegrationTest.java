package com.william.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.william.model.Account;
import com.william.model.request.TransactionDto;
import com.william.model.response.StatementResponse;
import com.william.repository.AccountRepository;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransactionControllerIntegrationTest {

    private static final String LOCALHOST = "https://localhost:";
    private static final String BALANCE = "/balance";
    private static final String TRANSFER = "/transfer";

    @LocalServerPort
    private int randomServerPort;

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private AccountRepository accountRepository;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        RestAssuredMockMvc.webAppContextSetup(context);
        objectMapper = new ObjectMapper();
    }

    @AfterEach
    public void cleanUp() {
        RestAssuredMockMvc.reset();
    }

    @Test
    @Transactional
    public void testMakeTransaction() {
        TransactionDto transactionDto =
                TransactionDto.builder().fromAccountId(1L).toAccountId(2L).amount(BigDecimal.valueOf(100)).build();

        verifyPostRequest(transactionDto, HttpStatus.OK.value());

        Account account1 = accountRepository.findById(1L).get();
        Account account2 = accountRepository.findById(2L).get();
        assertEquals(BigDecimal.valueOf(150.00).setScale(2), account1.getBalance());
        assertEquals(BigDecimal.valueOf(600.00).setScale(2), account2.getBalance());
    }

    @Test
    @Transactional
    public void testViewTransaction() throws IOException {
        Map<String, Long> params = new HashMap<>();
        params.put("accountId", 1L);

        MockMvcResponse response = verifyGetRequest(params, HttpStatus.OK.value());

        StatementResponse statementResponse = objectMapper.readValue(response.getBody().asInputStream(), StatementResponse.class);
        assertEquals(BigDecimal.valueOf(250.00).setScale(2), statementResponse.getBalance());
        assertTrue(statementResponse.getTransactionList().isEmpty());
    }

    private void verifyPostRequest(Object request, int statusCode) {
        given().body(request)
                .header("Content-Type", "application/json")
                .when()
                .post(LOCALHOST + randomServerPort + TRANSFER)
                .then()
                .statusCode(statusCode)
                .extract().response();
    }

    private MockMvcResponse verifyGetRequest(Map<String, Long> params, int statusCode) {
        return given().params(params)
                .when()
                .get(LOCALHOST + randomServerPort + BALANCE)
                .then()
                .statusCode(statusCode)
                .extract().response();
    }
}

