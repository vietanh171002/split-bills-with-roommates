package com.vietanh.expense_management.dto.request;

import lombok.Getter;

@Getter
public class ChangePasswordDto {
    private String oldPassword;
    private String newPassword;
}
