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

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.HashSet;

@Service
@Transactional
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    private final MemberRoomRepository memberRoomRepository;

    private final UserRepository userRepository;

    private final UserService userService;

    //math context
    MathContext mathContext = new MathContext(10, RoundingMode.HALF_UP);


    //create room
    @Override
    public Room createRoom(String roomName) {
        //get logged-in user 
        User user = userService.getUserFromSecurity();
        String userName = user.getName();

        Room room = Room.builder()
                .roomName(roomName)
                .members(new HashSet<>())
//                .expenses(new HashSet<>())
                .build();

        roomRepository.save(room);

        MemberRoom memberRoom = MemberRoom.builder()
                .balance(BigDecimal.ZERO)
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
    public void editRoomName(int roomId, String newRoomName) {
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

    //add user to room by id
    // Only members of room could add user to that room
    @Override
    public void addUserToRoom(int roomId, int addedUserId) {
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
                .balance(BigDecimal.ZERO)
                .memberRole(MemberRole.MEMBER)
                .room(room)
                .user(addedUser)
                .build();

        memberRoomRepository.save(addedMemberRoom);

        room.getMembers().add(addedMemberRoom);
        addedUser.getRooms().add(addedMemberRoom);

    }

    //add user to room by email
    @Override
    public void addUserToRoomByEmail(int RoomId, String addedUserEmail) {
        User addedUser = userRepository.findByEmail(addedUserEmail).orElseThrow(
                () -> new UserNotFoundException("User not found")
        );
        addUserToRoom(RoomId, addedUser.getId());
    }

    //remove member
    // only room owner could remove member
    @Override
    public void removeUserFromRoom(int roomId, int removedUserId) {
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

        //remove
        room.getMembers().remove(removedMemberRoom);

        //recalculate balances
        recalculateBalances(room);

        //finally delete removedMemberRoom
        memberRoomRepository.delete(removedMemberRoom);


    }

//change room owner
    @Override
    public void changeRoomOwner(int roomId, int newOwnerId) {
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

    }

    //leave room
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

        //member have negative balance can't leave room
        if(memberRoom.getBalance().compareTo(BigDecimal.ZERO) < 0){
            throw new ActionDeniedException("Your balance is negative, so you can't leave room ");
        }

        //remove memberRoom
        room.getMembers().remove(memberRoom);

        //recalculate balances
        recalculateBalances(room);

        //do leave room: delete memberRoom, cascade all spending of memberRoom get deleted
        memberRoomRepository.delete(memberRoom);
    }

    private void recalculateBalances(Room room){
        BigDecimal totalBalance = BigDecimal.ZERO;
        for (MemberRoom mem : room.getMembers()) {
            totalBalance = totalBalance.add(mem.getBalance());
        }

        // Calculate the adjustment per member
        BigDecimal adjustmentPerMember = totalBalance.divide(BigDecimal.valueOf(room.getMemberCount()), mathContext);

        // Update the balances for each member
        for (MemberRoom mem : room.getMembers()) {
            BigDecimal newBalance = mem.getBalance().subtract(adjustmentPerMember);
            mem.setBalance(newBalance);
        }
    }
}
