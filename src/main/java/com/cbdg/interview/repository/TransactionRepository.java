package com.cbdg.interview.repository;

import com.cbdg.interview.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountIdAndDateBetween(Long accountId, LocalDate start, LocalDate end);
    List<Transaction> findByAccountId(Long accountId);

}
