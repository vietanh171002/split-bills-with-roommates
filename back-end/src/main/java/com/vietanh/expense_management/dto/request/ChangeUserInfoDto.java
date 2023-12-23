package com.vietanh.expense_management.dto.request;

import lombok.Getter;

@Getter
public class ChangeUserInfoDto {
    private String newEmail;
    private String newName;
}
