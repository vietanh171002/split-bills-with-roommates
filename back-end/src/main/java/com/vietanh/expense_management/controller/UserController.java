package com.vietanh.expense_management.controller;

import com.vietanh.expense_management.dto.request.ChangePasswordDto;
import com.vietanh.expense_management.dto.request.ChangeUserInfoDto;
import com.vietanh.expense_management.dto.request.LoginDto;
import com.vietanh.expense_management.dto.request.RegisterDto;
import com.vietanh.expense_management.dto.response.TokenDto;
import com.vietanh.expense_management.dto.response.UserDto;
import com.vietanh.expense_management.model.User;
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
            return ResponseEntity.badRequest().body("Please provide enough information");
        }
        String token = userService.register(registerDto);
        TokenDto tokenDto = new TokenDto(token);
        return ResponseEntity.ok().body(tokenDto);
    }

    //login 
    @PostMapping(value = "login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        TokenDto tokenDto = userService.login(loginDto);
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
    public ResponseEntity<?> editUserInfo(@RequestBody ChangeUserInfoDto editDto){
        if (editDto.getNewName().isBlank()  ||
                editDto.getNewEmail().isBlank()
            ){
            return ResponseEntity.badRequest().body("Please provide enough information");
        }
        User user = userService.getUserFromSecurity();
        if(!user.getEmail().equals(editDto.getNewEmail())){
            userService.editUserInfo(editDto);
            return ResponseEntity.ok().body("ok");
        }
        else {
            userService.editUserInfo(editDto);
            return ResponseEntity.noContent().build();
        }
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

    //change password
    @PutMapping(value = "change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDto dto){
        userService.changePassword(dto);
        return ResponseEntity.noContent().build();
    }
}
