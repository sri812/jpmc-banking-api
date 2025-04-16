package com.cbdg.interview.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MonthlyStatementTransactionDto(
        String type,
        BigDecimal amount,
        String description,
        LocalDate date
) {}
