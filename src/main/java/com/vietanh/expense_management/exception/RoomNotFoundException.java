package com.vietanh.expense_management.exception;

public class RoomNotFoundException extends RuntimeException {
    public RoomNotFoundException() {
    }

    public RoomNotFoundException(String message) {
        super(message);
    }
}
