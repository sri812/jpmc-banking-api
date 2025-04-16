package com.cbdg.interview.controller;

import com.cbdg.interview.dto.AccountDto;
import com.cbdg.interview.dto.TransactionDto;
import com.cbdg.interview.service.BankingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BankingController.class)
class BankingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BankingService service;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @WithMockUser(roles = "user") // wrong role
    void testCreateAccount_withWrongRole_shouldReturnForbidden() throws Exception {
        AccountDto input = new AccountDto(null, "12345", "SAVINGS", BigDecimal.valueOf(1000));

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "new_app_role")
    void testGetTransactions_withValidRole_shouldReturnTransactions() throws Exception {
        TransactionDto tx = new TransactionDto("credit", BigDecimal.valueOf(200), "Salary", LocalDate.of(2025, 4, 15));
        when(service.getTransactionsByAccount(1L)).thenReturn(List.of(tx));

        mockMvc.perform(get("/api/accounts/1/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].type").value("credit"))
                .andExpect(jsonPath("$[0].amount").value(200))
                .andExpect(jsonPath("$[0].description").value("Salary"));
    }

    @Test
    void testGetTransactions_withoutAuth_shouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/accounts/1/transactions"))
                .andExpect(status().isUnauthorized());
    }


}