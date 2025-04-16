package com.cbdg.interview.controller;

import com.cbdg.interview.dto.AccountDto;
import com.cbdg.interview.dto.MonthlyStatementTransactionDto;
import com.cbdg.interview.dto.TransactionDto;
import com.cbdg.interview.service.BankingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
@Slf4j
public class BankingController {

    @Autowired
    private final BankingService service;

    /**
     * Creates a new bank account.
     *
     * @param dto the account details
     * @return the created account information
     */
    @PostMapping("/accounts")
    public ResponseEntity<AccountDto> createAccount(@RequestBody @Valid AccountDto dto) {
        log.info("Received request to create account: {}", dto);
        AccountDto account = service.createAccount(dto);
        log.info("Account created with ID: {}", account.id());
        return ResponseEntity.ok(account);
    }

    /**
     * Retrieves a list of all accounts.
     *
     * @return list of all accounts
     */
    @GetMapping("/accounts")
    public List<AccountDto> getAccounts() {
        log.info("Fetching all accounts");
        return service.getAllAccounts();
    }

    /**
     * Retrieves a specific account by its ID.
     *
     * @param id the account ID
     * @return the account details
     */
    @GetMapping("/accounts/{id}")
    public AccountDto getAccount(@PathVariable Long id) {
        log.info("Fetching account with ID: {}", id);
        return service.getAccount(id);
    }

    /**
     * Creates a transaction for a specific account.
     * <p><b>Note:</b> Transactions are created based on the provided account ID.</p>
     *
     * @param accountId the ID of the account the transaction belongs to
     * @param dto       the transaction details
     * @return the created transaction
     */
    @PostMapping("/accounts/{accountId}/transactions")
    public ResponseEntity<TransactionDto> createTransaction(
            @PathVariable Long accountId,
            @RequestBody @Valid TransactionDto dto
    ) {
        log.info("Creating transaction for account ID: {}", accountId);
        TransactionDto transaction = service.createTransaction(accountId, dto);
        log.info("Transaction created: {}", transaction);
        return ResponseEntity.ok(transaction);
    }

    /**
     * Retrieves all transactions for a specific account.
     *
     * @param accountId the ID of the account
     * @return list of transactions for the account
     */
    @GetMapping("/accounts/{accountId}/transactions")
    public ResponseEntity<List<TransactionDto>> getTransactionsByAccount(@PathVariable Long accountId) {
        log.info("Fetching transactions for account ID: {}", accountId);
        List<TransactionDto> transactions = service.getTransactionsByAccount(accountId);
        log.info("Found {} transactions for account ID: {}", transactions.size(), accountId);
        return ResponseEntity.ok(transactions);
    }

    /**
     * Retrieves the monthly transaction statement for an account.
     * <p>Format of the month should be <b>yyyy-MM</b> (e.g., 2025-04)</p>
     *
     * @param accountId the ID of the account
     * @param month     the year-month to filter transactions by (format: yyyy-MM)
     * @return list of transactions for the given month
     */
    @GetMapping("/statements/{accountId}")
    public List<MonthlyStatementTransactionDto> getStatement(
            @PathVariable Long accountId,
            @RequestParam String month
    ) {
        log.info("Generating monthly statement for account ID: {} and month: {}", accountId, month);
        YearMonth yearMonth = YearMonth.parse(month);
        List<MonthlyStatementTransactionDto> statement = service.getMonthlyStatement(accountId, yearMonth);
        log.info("Returning {} transactions for monthly statement", statement.size());
        return statement;
    }
}