package com.vietanh.expense_management.exception;

public class SpendingNotFoundException extends RuntimeException {
    public SpendingNotFoundException() {
    }

    public SpendingNotFoundException(String msg) {
        super(msg);
    }
}
