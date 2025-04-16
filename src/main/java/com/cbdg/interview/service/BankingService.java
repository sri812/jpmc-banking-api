package com.cbdg.interview.service;

import com.cbdg.interview.dto.AccountDto;
import com.cbdg.interview.dto.MonthlyStatementTransactionDto;
import com.cbdg.interview.dto.TransactionDto;
import com.cbdg.interview.exception.AccountNotFoundException;
import com.cbdg.interview.exception.TransactionsNotFoundException;
import com.cbdg.interview.model.Account;
import com.cbdg.interview.model.Transaction;
import com.cbdg.interview.repository.AccountRepository;
import com.cbdg.interview.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BankingService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public AccountDto createAccount(AccountDto dto) {
        log.info("Creating new account with number: {}", dto.accountNumber());

        Account account = new Account();
        account.setAccountNumber(dto.accountNumber());
        account.setAccountType(dto.accountType());
        account.setBalance(dto.balance());

        Account saved = accountRepository.save(account);
        log.info("Account created successfully with ID: {}", saved.getId());

        return new AccountDto(saved.getId(), saved.getAccountNumber(), saved.getAccountType(), saved.getBalance());
    }

    public List<AccountDto> getAllAccounts() {
        log.info("Fetching all accounts...");

        List<Account> accounts = accountRepository.findAll();

        if (accounts.isEmpty()) {
            log.warn("No accounts found.");
            throw new AccountNotFoundException("No accounts found.", "ACCOUNT_NOT_FOUND");
        }

        log.info("Found {} accounts.", accounts.size());

        return accounts.stream()
                .map(a -> new AccountDto(a.getId(), a.getAccountNumber(), a.getAccountType(), a.getBalance()))
                .collect(Collectors.toList());
    }

    public AccountDto getAccount(Long accountId) {
        log.info("Fetching account with ID: {}", accountId);

        Optional<Account> account = accountRepository.findById(accountId);

        if (account.isEmpty()) {
            log.error("Account with ID {} not found.", accountId);
            throw new AccountNotFoundException("Account with ID: " + accountId + " not found.", "ACCOUNT_NOT_FOUND");
        }

        Account a = account.get();
        log.info("Account found: ID = {}, Number = {}", a.getId(), a.getAccountNumber());

        return new AccountDto(a.getId(), a.getAccountNumber(), a.getAccountType(), a.getBalance());
    }

    public TransactionDto createTransaction(Long accountId, TransactionDto dto) {
        log.info("Creating transaction for account ID: {}", accountId);

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> {
                    log.error("Account with ID {} not found. Cannot create transaction.", accountId);
                    return new AccountNotFoundException("Account with ID: " + accountId + " not found.", "ACCOUNT_NOT_FOUND");
                });

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(dto.amount());
        transaction.setDate(dto.date());
        transaction.setDescription(dto.description());
        transaction.setType(dto.type());

        Transaction saved = transactionRepository.save(transaction);
        log.info("Transaction created successfully for account ID: {}", accountId);

        return new TransactionDto(
                saved.getType(),
                saved.getAmount(),
                saved.getDescription(),
                saved.getDate()
        );
    }

    public List<TransactionDto> getTransactionsByAccount(Long accountId) {
        log.info("Fetching transactions for account ID: {}", accountId);

        Optional<Account> account = accountRepository.findById(accountId);
        if (account.isEmpty()) {
            log.error("Account with ID {} not found.", accountId);
            throw new AccountNotFoundException("Account with ID: " + accountId + " not found.", "ACCOUNT_NOT_FOUND");
        }

        List<Transaction> transactions = transactionRepository.findByAccountId(accountId);
        if (transactions.isEmpty()) {
            log.warn("No transactions found for account ID: {}", accountId);
            throw new TransactionsNotFoundException("No transactions found for account with ID: " + accountId, "TRANSACTION_NOT_FOUND");
        }

        log.info("Found {} transactions for account ID: {}", transactions.size(), accountId);

        return transactions.stream()
                .map(t -> new TransactionDto(
                        t.getType(),
                        t.getAmount(),
                        t.getDescription(),
                        t.getDate()
                ))
                .collect(Collectors.toList());
    }

    public List<MonthlyStatementTransactionDto> getMonthlyStatement(Long accountId, YearMonth month) {
        log.info("Generating monthly statement for account ID: {}, month: {}", accountId, month);

        LocalDate start = month.atDay(1);
        LocalDate end = month.atEndOfMonth();

        List<Transaction> transactions = transactionRepository.findByAccountIdAndDateBetween(accountId, start, end);

        log.info("Found {} transactions for account ID: {} between {} and {}", transactions.size(), accountId, start, end);

        return transactions.stream()
                .map(t -> new MonthlyStatementTransactionDto(
                        t.getType(),
                        t.getAmount(),
                        t.getDescription(),
                        t.getDate()
                ))
                .collect(Collectors.toList());
    }
}