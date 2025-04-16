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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BankingService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    public AccountDto createAccount(AccountDto dto) {
        Account account = new Account();
        account.setAccountNumber(dto.accountNumber());
        account.setAccountType(dto.accountType());
        account.setBalance(dto.balance());

        Account saved = accountRepository.save(account);
        return new AccountDto(saved.getId(), saved.getAccountNumber(), saved.getAccountType(), saved.getBalance());
    }

    public List<AccountDto> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();

        if (accounts.isEmpty()) {
            throw new AccountNotFoundException("No accounts found.", "ACCOUNT_NOT_FOUND");
        }
        return accounts.stream()
                .map(a -> new AccountDto(a.getId(), a.getAccountNumber(), a.getAccountType(), a.getBalance()))
                .collect(Collectors.toList());
    }

    public AccountDto getAccount(Long accountId) {
        // Check if the account exists
        Optional<Account> account = accountRepository.findById(accountId);

        if (account.isEmpty()) {
            throw new AccountNotFoundException("Account with ID: " + accountId + " not found.", "ACCOUNT_NOT_FOUND");
        }

        // Return the account as DTO if found
        Account a = account.get();
        return new AccountDto(a.getId(), a.getAccountNumber(), a.getAccountType(), a.getBalance());
    }

    public TransactionDto createTransaction(Long accountId, TransactionDto dto) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account with ID: " + accountId + " not found.", "ACCOUNT_NOT_FOUND"));

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(dto.amount());
        transaction.setDate(dto.date());
        transaction.setDescription(dto.description());
        transaction.setType(dto.type());

        Transaction saved = transactionRepository.save(transaction);
        return new TransactionDto(
                saved.getType(),
                saved.getAmount(),
                saved.getDescription(),
                saved.getDate()
        );
    }

    public List<TransactionDto> getTransactionsByAccount(Long accountId) {
        // Check if the account exists first
        Optional<Account> account = accountRepository.findById(accountId);
        if (account.isEmpty()) {
            throw new AccountNotFoundException("Account with ID: " + accountId + " not found.", "ACCOUNT_NOT_FOUND");
        }

        // Fetch transactions for the account
        List<Transaction> transactions = transactionRepository.findByAccountId(accountId);

        // If no transactions found for the account
        if (transactions.isEmpty()) {
            throw new TransactionsNotFoundException("No transactions found for account with ID: " + accountId, "TRANSACTION_NOT_FOUND");
        }

        // Map the transaction entities to DTOs and return
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
        LocalDate start = month.atDay(1);
        LocalDate end = month.atEndOfMonth();

        List<Transaction> transactions = transactionRepository.findByAccountIdAndDateBetween(accountId, start, end);

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