package com.vietanh.expense_management.controller;

import com.vietanh.expense_management.dto.request.LoginDto;
import com.vietanh.expense_management.dto.request.RegisterDto;
import com.vietanh.expense_management.dto.response.UserDto;
import com.vietanh.expense_management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //register
    @PostMapping(value = "register")
    public ResponseEntity<?> register(@RequestBody RegisterDto registerDto) {
        String token = userService.register(registerDto);
        return ResponseEntity.ok().body(token);
    }

    //login 
    @PostMapping(value = "login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        String token = userService.login(loginDto);
        return ResponseEntity.ok().body(token);
    }

    //get my info
    @GetMapping(value = "info")
    public ResponseEntity<?> getUserInfo() {
        UserDto userDto = userService.getUserInfo();
        return ResponseEntity.ok().body(userDto);
    }

    //logout
    @DeleteMapping(value = "logout")
    public ResponseEntity<?> logout() {
        userService.logout();
        return ResponseEntity.ok().body("Logout successfully");
    }

    //delete account
    @DeleteMapping(value = "delete-account")
    public ResponseEntity<?> deleteAccount() {
        userService.deleteAccount();
        return ResponseEntity.ok().body("Account deleted");
    }
}
