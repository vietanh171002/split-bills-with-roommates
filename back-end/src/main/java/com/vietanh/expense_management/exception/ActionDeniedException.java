package com.vietanh.expense_management.exception;

public class ActionDeniedException extends RuntimeException {
    public ActionDeniedException() {
    }

    public ActionDeniedException(String msg) {
        super(msg);
    }
}
