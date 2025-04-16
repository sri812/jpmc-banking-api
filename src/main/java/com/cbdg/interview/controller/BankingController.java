package com.cbdg.interview.controller;

import com.cbdg.interview.dto.AccountDto;
import com.cbdg.interview.dto.MonthlyStatementTransactionDto;
import com.cbdg.interview.dto.TransactionDto;
import com.cbdg.interview.service.BankingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
public class BankingController {

    @Autowired
    private BankingService service;

    @PostMapping("/accounts")
    public ResponseEntity<AccountDto> createAccount(@RequestBody @Valid AccountDto dto) {
        return ResponseEntity.ok(service.createAccount(dto));
    }
    @GetMapping("/accounts")
    public List<AccountDto> getAccounts() {
        return service.getAllAccounts();
    }

    @GetMapping("/accounts/{id}")
    public AccountDto getAccount(@PathVariable Long id) {
        return service.getAccount(id);
    }


    @PostMapping("/accounts/{accountId}/transactions")
    public ResponseEntity<TransactionDto> createTransaction(
            @PathVariable Long accountId,
            @RequestBody @Valid TransactionDto dto
    ) {
        return ResponseEntity.ok(service.createTransaction(accountId, dto));
    }

    @GetMapping("accounts/{accountId}/transactions")
    public ResponseEntity<List<TransactionDto>> getTransactionsByAccount(@PathVariable Long accountId) {
        List<TransactionDto> transactions = service.getTransactionsByAccount(accountId);
        return ResponseEntity.ok(transactions);
    }

    // Endpoint to get monthly statement
    @GetMapping("/statements/{accountId}")
    public List<MonthlyStatementTransactionDto> getStatement(
            @PathVariable Long accountId,
            @RequestParam String month
    ) {
        YearMonth yearMonth = YearMonth.parse(month);
        return service.getMonthlyStatement(accountId, yearMonth);
    }
}
