package com.vietanh.expense_management.service.impl;

import com.vietanh.expense_management.exception.ActionDeniedException;
import com.vietanh.expense_management.exception.MemberRoomNotFoundException;
import com.vietanh.expense_management.exception.RoomNotFoundException;
import com.vietanh.expense_management.exception.UserNotFoundException;
import com.vietanh.expense_management.model.MemberRoom;
import com.vietanh.expense_management.model.Room;
import com.vietanh.expense_management.model.User;
import com.vietanh.expense_management.model.enumeration.MemberRole;
import com.vietanh.expense_management.repository.MemberRoomRepository;
import com.vietanh.expense_management.repository.RoomRepository;
import com.vietanh.expense_management.repository.UserRepository;
import com.vietanh.expense_management.service.RoomService;
import com.vietanh.expense_management.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@Transactional
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    private final MemberRoomRepository memberRoomRepository;

    private final UserRepository userRepository;

    private final UserService userService;

    //create room
    @Override
    public Room createRoom(String roomName) {
        //get logged-in user 
        User user = userService.getUserFromSecurity();
        String userName = user.getName();

        Room room = Room.builder()
                .roomName(roomName)
                .members(new HashSet<>())
                .owner(userName)
                .totalSpending(0)
                .build();

        roomRepository.save(room);

        MemberRoom memberRoom = MemberRoom.builder()
                .balance(0)
                .spentAmount(0)
                .room(room)
                .user(user)
                .memberRole(MemberRole.OWNER)
                .spendings(new HashSet<>())
                .build();

        memberRoomRepository.save(memberRoom);

        room.getMembers().add(memberRoom);
        user.getRooms().add(memberRoom);

        return room;
    }

    //edit room name
    @Override
    public Room editRoomName(int roomId, String newRoomName) {
        //get logged-in user 
        User user = userService.getUserFromSecurity();

        //get room
        Room room = roomRepository.findByRoomId(roomId).orElseThrow(
                () -> new RoomNotFoundException("Room not found")
        );

        //only room Owner could rename room 
        MemberRoom memberRoom = memberRoomRepository.findByRoomAndUser(room, user).orElseThrow(
                () -> new MemberRoomNotFoundException("You aren't member of this room")
        );
        if (!memberRoom.getMemberRole().equals(MemberRole.OWNER)) {
            throw new ActionDeniedException("Only room owner could rename room");
        }

        room.setRoomName(newRoomName);
        // roomRepository.save(room);

        return room;
    }

    //delete room
    @Override
    public void deleteRoom(int roomId) {
        //get logged-in user 
        User user = userService.getUserFromSecurity();

        //get room
        Room room = roomRepository.findByRoomId(roomId).orElseThrow(
                () -> new RoomNotFoundException("Room not found")
        );

        //get memberRoom of the logged-in user and the room
        MemberRoom memberRoom = memberRoomRepository.findByRoomAndUser(room, user).orElseThrow(
                () -> new MemberRoomNotFoundException("You aren't member of this room")
        );

        //ensure that the logged-in user is room owner
        if (!memberRoom.getMemberRole().equals(MemberRole.OWNER)) {
            throw new ActionDeniedException("Only room owner could delete");
        }

        //delete room
        roomRepository.delete(room);
    }

    //get room info
    @Override
    public Room getRoomInfo(int roomId) {
        User user = userService.getUserFromSecurity();
        Room room = roomRepository.findByRoomId(roomId).orElseThrow(
                () -> new RoomNotFoundException("Room not found")
        );

        if (!memberRoomRepository.existsByRoomAndUser(room, user)) {
            throw new MemberRoomNotFoundException("You aren't member of this room");
        }
        return room;
    }

    // Only members of room could add user to that room
    @Override
    public Room addUserToRoom(int roomId, int addedUserId) {
        //get logged-in user
        User user = userService.getUserFromSecurity();

        //get room
        Room room = roomRepository.findByRoomId(roomId).orElseThrow(
                () -> new RoomNotFoundException("Room not found")
        );

        //get added user
        User addedUser = userRepository.findById(addedUserId).orElseThrow(
                () -> new UserNotFoundException("Added user not found"));

        //check if the logged-in user is a room member
        if (!memberRoomRepository.existsByRoomAndUser(room, user)) {
            throw new MemberRoomNotFoundException("You aren't member of this room");
        }

        //ensure that the added user is not already a room member
        if (memberRoomRepository.existsByRoomAndUser(room, addedUser)) {
            throw new MemberRoomNotFoundException("Added user is already room member");
        }

        //add member
        MemberRoom addedMemberRoom = MemberRoom.builder()
                .spendings(new HashSet<>())
                .balance(0)
                .spentAmount(0)
                .memberRole(MemberRole.MEMBER)
                .room(room)
                .user(addedUser)
                .build();

        memberRoomRepository.save(addedMemberRoom);

        room.getMembers().add(addedMemberRoom);
        addedUser.getRooms().add(addedMemberRoom);

        return room;
    }

    // only room owner could remove member
    @Override
    public Room removeUserFromRoom(int roomId, int removedUserId) {
        //get logged-in user
        User user = userService.getUserFromSecurity();

        //get room
        Room room = roomRepository.findByRoomId(roomId).orElseThrow(
                () -> new RoomNotFoundException("Room not found")
        );

        //get memberRoom of the logged-in user and the room
        MemberRoom memberRoom = memberRoomRepository.findByRoomAndUser(room, user).orElseThrow(
                () -> new MemberRoomNotFoundException("You aren't member of this room")
        );

        //ensure that the logged-in user is room owner
        if (!memberRoom.getMemberRole().equals(MemberRole.OWNER)) {
            throw new ActionDeniedException("Only room owner could remove member");
        }

        //ensure that room owner doesn't remove themselves
        if (user.getId() == removedUserId) {
            throw new ActionDeniedException("You couldn't remove yourself");
        }

        //get removed user
        User removedUser = userRepository.findById(removedUserId).orElseThrow(
                () -> new UserNotFoundException("Removed user not found")
        );

        //get memberRoom of the removed member and the room
        MemberRoom removedMemberRoom = memberRoomRepository.findByRoomAndUser(room, removedUser).orElseThrow(
                () -> new MemberRoomNotFoundException("The member you want to remove aren't member of this room")
        );


        //finally delete removedMemberRoom
        memberRoomRepository.delete(removedMemberRoom);

        return room;
    }


    @Override
    public Room changeRoomOwner(int roomId, int newOwnerId) {
        //get logged-in user
        User user = userService.getUserFromSecurity();

        //get room
        Room room = roomRepository.findByRoomId(roomId).orElseThrow(
                () -> new RoomNotFoundException("Room not found")
        );

        //get memberRoom of the logged-in user and the room
        MemberRoom memberRoom = memberRoomRepository.findByRoomAndUser(room, user).orElseThrow(
                () -> new MemberRoomNotFoundException("You aren't member of this room")
        );

        //ensure that the logged-in user is room owner
        if (!memberRoom.getMemberRole().equals(MemberRole.OWNER)) {
            throw new ActionDeniedException("Only room owner could change room owner");
        }

        //get newOwner things
        User newOwner = userRepository.findById(newOwnerId).orElseThrow(
                () -> new UserNotFoundException("Room not found")
        );

        MemberRoom newOwnerMemberRoom = memberRoomRepository.findByRoomAndUser(room, newOwner).orElseThrow(
                () -> new MemberRoomNotFoundException("This user aren't room member")
        );

        //change owner
        memberRoom.setMemberRole(MemberRole.MEMBER);
        newOwnerMemberRoom.setMemberRole(MemberRole.OWNER);
        room.setOwner(newOwner.getName());

        return room;
    }

    @Override
    public void leaveRoom(int roomId) {
        //get logged-in user
        User user = userService.getUserFromSecurity();

        //get room
        Room room = roomRepository.findByRoomId(roomId).orElseThrow(
                () -> new RoomNotFoundException("Room not found")
        );

        //get memberRoom
        MemberRoom memberRoom = memberRoomRepository.findByRoomAndUser(room, user).orElseThrow(
                () -> new MemberRoomNotFoundException("You aren't member of this room")
        );

        //room owner couldn't leave room
        if (memberRoom.getMemberRole().equals(MemberRole.OWNER)) {
            throw new ActionDeniedException("You are room owner, so you couldn't leave room");
        }

        //do leave room: delete memberRoom, cascade all spending of memberRoom get deleted
        memberRoomRepository.delete(memberRoom);
    }
}
