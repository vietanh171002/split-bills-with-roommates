package com.vietanh.expense_management.service;

import com.vietanh.expense_management.dto.request.ChangePasswordDto;
import com.vietanh.expense_management.dto.request.ChangeUserInfoDto;
import com.vietanh.expense_management.dto.request.LoginDto;
import com.vietanh.expense_management.dto.request.RegisterDto;
import com.vietanh.expense_management.dto.response.TokenDto;
import com.vietanh.expense_management.dto.response.UserDto;
import com.vietanh.expense_management.model.User;

public interface UserService {
    String register(RegisterDto registerDto);

    TokenDto login(LoginDto loginDto);

    UserDto mapUserToUserDto(User user);

    UserDto getUserInfo();

    void editUserInfo(ChangeUserInfoDto editDto);

    User getUserFromSecurity();

    void deleteAccount();

    void logout();

    void changePassword(ChangePasswordDto dto);
}



