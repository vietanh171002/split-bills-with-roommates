package com.vietanh.expense_management.dto.request;

import lombok.Data;

@Data
public class RegisterDto {

    private String name;
    private String email;
    private String password;
}
