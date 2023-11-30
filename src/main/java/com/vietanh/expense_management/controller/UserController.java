package com.vietanh.expense_management.controller;

import com.vietanh.expense_management.dto.request.LoginDto;
import com.vietanh.expense_management.dto.request.RegisterDto;
import com.vietanh.expense_management.dto.response.TokenDto;
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
        if (registerDto.getName().isBlank()  ||
                registerDto.getEmail().isBlank()||
                registerDto.getPassword().isBlank())  {
            return ResponseEntity.badRequest().body("Please provide enough info");
        }
        String token = userService.register(registerDto);
        TokenDto tokenDto = new TokenDto(token);
        return ResponseEntity.ok().body(tokenDto);
    }

    //login 
    @PostMapping(value = "login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        String token = userService.login(loginDto);
        TokenDto tokenDto = new TokenDto(token);
        return ResponseEntity.ok().body(tokenDto);
    }

    //get my info
    @GetMapping(value = "info")
    public ResponseEntity<?> getUserInfo() {
        UserDto userDto = userService.getUserInfo();
        return ResponseEntity.ok().body(userDto);
    }

    //edit user info
    @PutMapping(value = "edit-info")
    public ResponseEntity<?> editUserInfo(@RequestBody RegisterDto editDto){
        if (editDto.getName().isBlank()  ||
                editDto.getEmail().isBlank()||
                editDto.getPassword().isBlank())  {
            return ResponseEntity.badRequest().body("Please provide enough info");
        }
        userService.editUserInfo(editDto);
        UserDto userDto = userService.getUserInfo();
        return  ResponseEntity.ok().body(userDto);
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
