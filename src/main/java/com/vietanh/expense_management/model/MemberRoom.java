package com.vietanh.expense_management.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.vietanh.expense_management.model.composite_key.MemberRoomId;
import com.vietanh.expense_management.model.enumeration.MemberRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(MemberRoomId.class)
@Table(name = "member_room")
public class MemberRoom {
    private int balance;
    private int spentAmount;
    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    @JsonManagedReference
    @OneToMany(mappedBy = "memberRoom", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Spending> spendings = new HashSet<>();

    @Id
    @JsonBackReference
    @ManyToOne(optional = false)
    @JoinColumn(name = "room_id")
    private Room room;

    @Id
    @JsonBackReference
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;
}
