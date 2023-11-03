package com.vietanh.expense_management.controller;

import com.vietanh.expense_management.dto.request.CreateRoomDto;
import com.vietanh.expense_management.dto.response.UserDto;
import com.vietanh.expense_management.model.Room;
import com.vietanh.expense_management.service.RoomService;
import com.vietanh.expense_management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/room")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    private final UserService userService;

    //create room
    @PostMapping(value = "create")
    public ResponseEntity<?> createRoom(@RequestBody CreateRoomDto createRoomDto) {
        String roomName = createRoomDto.getRoomName();
        Room room = roomService.createRoom(roomName);
        return ResponseEntity.ok().body(room);
    }

    //edit room name
    @PutMapping(value = "/{roomId}/edit-room-name")
    public ResponseEntity<?> editRoomName(@PathVariable int roomId, @RequestBody CreateRoomDto createRoomDto) {
        String newRoomName = createRoomDto.getRoomName();
        Room room = roomService.editRoomName(roomId, newRoomName);
        return ResponseEntity.ok().body(room);
    }

    //get room info 
    @GetMapping(value = "/{roomId}/info")
    public ResponseEntity<?> getRoomInfo(@PathVariable int roomId) {
        Room room = roomService.getRoomInfo(roomId);
        return ResponseEntity.ok().body(room);
    }

    //add member to room
    @PostMapping(value = "/{roomId}/add-member/{addedUserId}")
    public ResponseEntity<?> addUserToRoom(@PathVariable int roomId, @PathVariable int addedUserId) {
        Room room = roomService.addUserToRoom(roomId, addedUserId);
        return ResponseEntity.ok().body(room);
    }

    //remove member from room 
    @DeleteMapping(value = "/{roomId}/remove-member/{removedMemberId}")
    public ResponseEntity<?> removeMemberFromRoom(@PathVariable int roomId, @PathVariable int removedMemberId) {
        Room room = roomService.removeUserFromRoom(roomId, removedMemberId);
        return ResponseEntity.ok().body(room);
    }

    //change room owner
    @PutMapping(value = "/{roomId}/change-room-owner/{newOwnerId}")
    public ResponseEntity<?> changeRoomOwner(@PathVariable int roomId, @PathVariable int newOwnerId) {
        Room room = roomService.changeRoomOwner(roomId, newOwnerId);
        return ResponseEntity.ok().body(room);
    }

    //delete room
    @DeleteMapping(value = "/{roomId}/delete")
    public ResponseEntity<?> deleteRoom(@PathVariable int roomId) {
        roomService.deleteRoom(roomId);
        return ResponseEntity.ok().body("Delete room successfully");
    }

    //leave room
    @PutMapping(value = "/{roomId}/leave")
    public ResponseEntity<?> leaveRoom(@PathVariable int roomId) {
        roomService.leaveRoom(roomId);
        UserDto userDto = userService.getUserInfo();
        return ResponseEntity.ok().body(userDto);
    }
}

