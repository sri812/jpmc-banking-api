package com.cbdg.interview.dto;

import java.math.BigDecimal;

public record AccountDto(Long id, String accountNumber, String accountType, BigDecimal balance) {}
