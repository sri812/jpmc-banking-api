package com.cbdg.interview.repository;

import com.cbdg.interview.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {}

