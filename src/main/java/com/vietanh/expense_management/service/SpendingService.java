package com.vietanh.expense_management.service;

import com.vietanh.expense_management.dto.request.SpendingDto;
import com.vietanh.expense_management.model.Room;
import com.vietanh.expense_management.model.Spending;
import com.vietanh.expense_management.model.enumeration.SpendingCategory;

import java.time.LocalDate;

public interface SpendingService {
    Room createSpending(int RoomId, String detail, int amount, LocalDate date, SpendingCategory category);

    Spending getSpending(int spendingId);

    Spending editSpending(int spendingId, SpendingDto editDto);

    void deleteSpending(int spendingId);
}
