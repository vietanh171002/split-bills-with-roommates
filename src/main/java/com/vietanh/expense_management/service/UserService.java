package com.vietanh.expense_management.service;

import com.vietanh.expense_management.dto.request.LoginDto;
import com.vietanh.expense_management.dto.request.RegisterDto;
import com.vietanh.expense_management.dto.response.UserDto;
import com.vietanh.expense_management.model.User;

public interface UserService {
    String register(RegisterDto registerDto);

    String login(LoginDto loginDto);

    UserDto mapUserToUserDto(User user);

    UserDto getUserInfo();

    User getUserFromSecurity();

    void deleteAccount();

    void logout();
}



