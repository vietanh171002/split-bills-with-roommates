package com.vietanh.expense_management.repository;

import com.vietanh.expense_management.model.MemberRoom;
import com.vietanh.expense_management.model.Room;
import com.vietanh.expense_management.model.User;
import com.vietanh.expense_management.model.composite_key.MemberRoomId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRoomRepository extends JpaRepository<MemberRoom, MemberRoomId> {
    Optional<MemberRoom> findByRoomAndUser(Room room, User user);

    boolean existsByRoomAndUser(Room room, User user);
}
