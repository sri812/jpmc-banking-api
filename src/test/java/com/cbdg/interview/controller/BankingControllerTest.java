package com.cbdg.interview.controller;

import com.cbdg.interview.dto.AccountDto;
import com.cbdg.interview.dto.TransactionDto;
import com.cbdg.interview.service.BankingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.Mockito.when;

@WebMvcTest(BankingController.class)
public class BankingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BankingService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateAccount() throws Exception {
        AccountDto dto = new AccountDto(null, "123", "SAVINGS", BigDecimal.valueOf(100));
        AccountDto saved = new AccountDto(1L, "123", "SAVINGS", BigDecimal.valueOf(100));

        when(service.createAccount(Mockito.any())).thenReturn(saved);

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testGetTransactions() throws Exception {
        TransactionDto tx = new TransactionDto("credit", BigDecimal.valueOf(100), "Salary", LocalDate.now());

        when(service.getTransactionsByAccount(1L)).thenReturn(List.of(tx));

        mockMvc.perform(get("/api/accounts/1/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }
}