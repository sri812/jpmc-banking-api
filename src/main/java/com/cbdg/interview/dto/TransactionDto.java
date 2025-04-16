package com.cbdg.interview.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionDto(
        @NotNull String type,
        @NotNull @Positive BigDecimal amount,
        String description,
        @NotNull LocalDate date
) {}
