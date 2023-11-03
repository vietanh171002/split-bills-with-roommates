package com.vietanh.expense_management.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

// @Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roomId;
    private String roomName;
    private String owner;
    private int totalSpending;

    @JsonManagedReference
    @OneToMany(mappedBy = "room", orphanRemoval = true)
    private Set<MemberRoom> members = new HashSet<>();

}
