package com.vietanh.expense_management.repository;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;

import org.assertj.core.api.Assertions;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.vietanh.expense_management.exception.MemberRoomNotFoundException;
import com.vietanh.expense_management.model.MemberRoom;
import com.vietanh.expense_management.model.Room;
import com.vietanh.expense_management.model.User;

@DataJpaTest
public class MemberRoomRepoTests {
    
    @Autowired
    private MemberRoomRepository memberRoomRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoomRepository roomRepository;

    @Test 
    public void testMemberRoomRepo1(){
        //case 1 
        User user = new User();
        Room room = new Room(); 
        userRepository.save(user);
        roomRepository.save(room);
        
        MemberRoom memberRoom = MemberRoom.builder()
                                            .room(room)
                                            .user(user)
                                            .build();
        
        memberRoomRepository.save(memberRoom);

        MemberRoom foundMemberRoom = memberRoomRepository.findByRoomAndUser(room, user).orElseThrow(()
            -> new MemberRoomNotFoundException());

        assertDoesNotThrow( () -> {
            memberRoomRepository.findByRoomAndUser(room, user).orElseThrow(()
            -> new MemberRoomNotFoundException());
        } );

        //case 2
        User user2 = new User();
        Room room2 = new Room(); 
        userRepository.save(user2);
        roomRepository.save(room2);
        
        assertThrows(MemberRoomNotFoundException.class, ()->{
            memberRoomRepository.findByRoomAndUser(room2, user2).orElseThrow(()
            -> new MemberRoomNotFoundException());
        } )  ;
    }

    @Test 
    public void testMemberRoomRepo2(){

        //case 1
        User user = new User();
        Room room = new Room(); 
        userRepository.save(user);
        roomRepository.save(room);
        
        MemberRoom memberRoom = MemberRoom.builder()
                                            .room(room)
                                            .user(user)
                                            .build();
        
        memberRoomRepository.save(memberRoom);

        Assertions.assertThat(memberRoomRepository.existsByRoomAndUser(room, user)).isTrue();

        //case 2 
         User user2 = new User();
        Room room2 = new Room(); 
        userRepository.save(user2);
        roomRepository.save(room2);

        Assertions.assertThat(memberRoomRepository.existsByRoomAndUser(room2, user2)).isFalse();        

    }

    @Test
    public void MemberRoomTest3(){
        User user = new User(); 
        Room room = new Room(); 
        userRepository.save(user); 
        roomRepository.save(room);

        MemberRoom memberRoom = MemberRoom.builder()
                                            .room(room)
                                            .user(user)
                                            .build(); 
        // user.setRooms(new HashSet<>());
        room.setMembers(new HashSet<>());

        // user.getRooms().add(memberRoom); 
        room.getMembers().add(memberRoom); 

        memberRoomRepository.save(memberRoom); 
         

        room.getMembers().remove(memberRoom); 
        roomRepository.save(room); 

        assertThat(memberRoomRepository.findAll()).isNull();

    }

}
