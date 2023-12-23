package com.vietanh.expense_management.service.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.vietanh.expense_management.model.enumeration.Role;
import com.vietanh.expense_management.model.User;
import com.vietanh.expense_management.repository.MemberRoomRepository;
import com.vietanh.expense_management.repository.RoomRepository;
import com.vietanh.expense_management.repository.UserRepository;
import com.vietanh.expense_management.service.UserService;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
// import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

// @SpringBootTest
class UserServiceImplTest{
    
    @MockBean
    private MemberRoomRepository memberRoomRepository;

    @MockBean
    private RoomRepository roomRepository;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void setUp() {
        UserDetails userDetails = User.builder()
                                    .email("vietanh@gmail.com")
                                    .password("abc123")
                                    .role(Role.USER)
                                    .build();
                                    
        TestingAuthenticationToken authentication = new TestingAuthenticationToken(userDetails, "credentials");
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    public void testAddUserToRoom(){
		String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Assertions.assertThat(userEmail).isEqualTo("vietanh@gmail.com");

		when(roomRepository.findByRoomId(anyInt()));
    }

    // public Room addUserToRoom(int RoomId, int addedUserId) {
	// 	Room room = roomRepository.findByRoomId(RoomId).orElseThrow(() -> new RoomNotFoundException());

	// 	String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
	// 	User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UserNotFoundException());
		
	// 	User addedUser = userRepository.findById(addedUserId).orElseThrow(
	// 		() -> new UserNotFoundException("Added user not found"));
		
	// 	if(!memberRoomRepository.existsByRoomAndUser(room, user)){
	// 		throw new MemberRoomNotFoundException("You aren't member of this room");
	// 	}

	// 	if(memberRoomRepository.existsByRoomAndUser(room, addedUser)){
	// 		throw new MemberRoomNotFoundException("Added user is already room member");
	// 	}

	// 	MemberRoom addedMemberRoom = MemberRoom.builder()
	// 											.balance(0)
	// 											.spentAmount(0)
	// 											.memberRole(MemberRole.MEMBER)
	// 											.room(room)
	// 											.user(addedUser)
	// 											.build();
		
	// 	memberRoomRepository.save(addedMemberRoom);

	// 	room.getMembers().add(addedMemberRoom);
	// 	addedUser.getRooms().add(addedMemberRoom);

	// 	roomRepository.save(room);
	// 	userRepository.save(addedUser);

	// 	return room; 
	// }
   
}

