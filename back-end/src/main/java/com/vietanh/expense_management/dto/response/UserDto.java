package com.vietanh.expense_management.dto.response;

import com.vietanh.expense_management.model.MemberRoom;
import lombok.Builder;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class UserDto {
    private int id;
    private String name;
    private String email;
    private Set<MemberRoom> rooms = new HashSet<>();
}
