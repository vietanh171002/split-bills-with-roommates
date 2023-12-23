package com.vietanh.expense_management.model.composite_key;

import com.vietanh.expense_management.model.Room;
import com.vietanh.expense_management.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberRoomId implements Serializable {
    private Room room;
    private User user;
}
 