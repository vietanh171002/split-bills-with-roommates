package com.vietanh.expense_management.service;

import com.vietanh.expense_management.model.Room;

public interface RoomService {
    Room createRoom(String roomName);

    Room editRoomName(int roomId, String newRoomName);

    void deleteRoom(int roomId);

    Room addUserToRoom(int roomId, int addedUserId);

    Room getRoomInfo(int roomId);

    Room removeUserFromRoom(int roomId, int removedUserId);

    Room changeRoomOwner(int roomId, int newOwnerId);

    void leaveRoom(int roomId);
}
