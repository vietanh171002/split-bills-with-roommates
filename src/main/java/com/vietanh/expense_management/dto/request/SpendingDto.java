package com.vietanh.expense_management.dto.request;

import com.vietanh.expense_management.model.enumeration.SpendingCategory;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SpendingDto {
    private String detail;
    private BigDecimal amount;
    private LocalDate date;
    private SpendingCategory category;
}
