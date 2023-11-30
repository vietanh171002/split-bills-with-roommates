package com.vietanh.expense_management.service.impl;

import com.vietanh.expense_management.dto.request.LoginDto;
import com.vietanh.expense_management.dto.request.RegisterDto;
import com.vietanh.expense_management.dto.response.UserDto;
import com.vietanh.expense_management.exception.ActionDeniedException;
import com.vietanh.expense_management.exception.UserNotFoundException;
import com.vietanh.expense_management.model.MemberRoom;
import com.vietanh.expense_management.model.User;
import com.vietanh.expense_management.model.enumeration.MemberRole;
import com.vietanh.expense_management.model.enumeration.Role;
import com.vietanh.expense_management.repository.UserRepository;
import com.vietanh.expense_management.security.JwtService;
import com.vietanh.expense_management.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    @Override
    public String register(RegisterDto registerDto) {

        //ensure that email isn't already in use
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new ActionDeniedException("Email is already in use");
        }

        //create user
        User user = User.builder()
                .email(registerDto.getEmail())
                .name(registerDto.getName())
                .role(Role.USER)
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .rooms(new HashSet<>())
                .build();
        userRepository.save(user);

        //generate token
        String token = jwtService.generateToken(registerDto.getEmail());
        return token;
    }

    @Override
    public String login(LoginDto loginDto) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(), loginDto.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtService.generateToken(loginDto.getEmail());

        return token;
    }

    @Override
    public UserDto mapUserToUserDto(User user) {
        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .rooms(user.getRooms())
                .build();
        return userDto;
    }

    @Override
    public User getUserFromSecurity() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(userEmail).orElseThrow(
                () -> new UserNotFoundException("SecurityContextHolder error")
        );
        return user;
    }

    @Override
    public void deleteAccount() {
        User user = getUserFromSecurity();

        //ensure that user left all rooms before deleting account
        if (!user.getRooms().isEmpty()) {
            throw new ActionDeniedException("You have to leave all rooms before deleting account");
        }
        //delete user
        userRepository.delete(user);
    }

    @Override
    public UserDto getUserInfo() {
        User user = getUserFromSecurity();
        UserDto userDto = mapUserToUserDto(user);
        return userDto;
    }

//edit user info
    @Override
    public void editUserInfo(RegisterDto editDto) {
        User user = getUserFromSecurity();


        //edit info
        user.setEmail(editDto.getEmail());
        user.setName(editDto.getName());
        user.setPassword(passwordEncoder.encode(editDto.getPassword()));
    }

    @Override
    public void logout() {
        SecurityContextHolder.clearContext();
    }

}
