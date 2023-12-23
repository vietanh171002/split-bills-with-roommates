package com.vietanh.expense_management.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Builder
public class LoginDto {
    private String email;
    private String password;
}
