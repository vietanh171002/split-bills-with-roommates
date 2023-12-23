package com.vietanh.expense_management.exception;

public class MemberRoomNotFoundException extends RuntimeException {
    public MemberRoomNotFoundException() {
    }

    public MemberRoomNotFoundException(String msg) {
        super(msg);
    }
}
