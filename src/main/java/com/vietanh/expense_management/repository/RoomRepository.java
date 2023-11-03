package com.vietanh.expense_management.repository;

import com.vietanh.expense_management.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface RoomRepository extends JpaRepository<Room, Integer> {
    Optional<Room> findByRoomId(int roomId);
}
