package com.vietanh.expense_management.service;

import com.vietanh.expense_management.model.Room;

public interface RoomService {
    Room createRoom(String roomName);

    void editRoomName(int roomId, String newRoomName);

    void deleteRoom(int roomId);

    void addUserToRoom(int roomId, int addedUserId);

    void addUserToRoomByEmail(int RoomId, String addedUserEmail);

    Room getRoomInfo(int roomId);

    void removeUserFromRoom(int roomId, int removedUserId);

    void changeRoomOwner(int roomId, int newOwnerId);

    void leaveRoom(int roomId);
}
