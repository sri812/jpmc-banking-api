package com.cbdg.interview.service;


import com.cbdg.interview.dto.AccountDto;

import com.cbdg.interview.model.Account;
import com.cbdg.interview.repository.AccountRepository;
import com.cbdg.interview.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BankingServiceTest {

    private BankingService service;
    private AccountRepository accountRepository;
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        // Create mocks
        accountRepository = mock(AccountRepository.class);
        transactionRepository = mock(TransactionRepository.class);

        // Create the service instance
        service = new BankingService();

        // Inject mocks using ReflectionTestUtils
        ReflectionTestUtils.setField(service, "accountRepository", accountRepository);
        ReflectionTestUtils.setField(service, "transactionRepository", transactionRepository);
    }

    @Test
    void testCreateAccount() {
        // Create a mock Account entity
        Account accountEntity = new Account(1L, "12345", "SAVINGS", new BigDecimal("1000"));

        // Mock the save behavior to return the created account
        when(accountRepository.save(any(Account.class))).thenReturn(accountEntity);

        // Create AccountDto
        AccountDto accountDto = new AccountDto(1L, "12345", "SAVINGS", new BigDecimal("1000"));

        // Call the service method
        AccountDto createdAccount = service.createAccount(accountDto);

        // Validate that the created AccountDto matches expected
        assertNotNull(createdAccount);
        assertEquals("12345", createdAccount.accountNumber());
        assertEquals("SAVINGS", createdAccount.accountType());
        assertEquals(new BigDecimal("1000"), createdAccount.balance());
    }

}