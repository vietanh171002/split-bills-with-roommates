package com.vietanh.expense_management.demo;



import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.HashSet;

import org.assertj.core.api.Assertions;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.vietanh.expense_management.exception.MemberRoomNotFoundException;
import com.vietanh.expense_management.exception.UserNotFoundException;
import com.vietanh.expense_management.model.MemberRoom;
import com.vietanh.expense_management.model.Room;
import com.vietanh.expense_management.model.User;
import com.vietanh.expense_management.repository.MemberRoomRepository;
import com.vietanh.expense_management.repository.RoomRepository;
import com.vietanh.expense_management.repository.UserRepository;
import com.vietanh.expense_management.service.UserService;

// @SpringBootTest
// @SpringJUnitConfig
public class DemoTests {

//     @Mock
//     private UserRepository userRepository;
//
//     @InjectMocks
//     private UserService userService;

    @Test
    public void testHashSet(){
        Room room = Room.builder()
                        .owner("banh")
                        .members(new HashSet<>())
                        .build();
        MemberRoom memberRoom = new MemberRoom();
        room.getMembers().add(memberRoom);
        // System.out.println(room.getMembers());
    }

    @Test
    public void test(){
        Room room = Room.builder()
                        .owner("banh")
                        .members(new HashSet<>())
                        .build();
        MemberRoom memberRoom = new MemberRoom();
        room.getMembers().add(memberRoom);
        // return ResponseEntity.ok().body(room);
    }

    @Test
    public void testExceptionNotNutt(){
        Assertions.assertThat(new UserNotFoundException()).isNotNull();
    }

    @Test
    public void testIncrementsIntProperty(){
        Room room = Room.builder()
                        .totalSpending(0)
                        .build();
        int amount = 10; 
        room.setTotalSpending(room.getTotalSpending()+ amount);

        assertThat(room.getTotalSpending()).isEqualTo(10);
    }
    
}
