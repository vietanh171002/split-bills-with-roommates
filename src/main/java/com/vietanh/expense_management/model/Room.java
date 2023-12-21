package com.vietanh.expense_management.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.vietanh.expense_management.exception.UserNotFoundException;
import com.vietanh.expense_management.model.enumeration.MemberRole;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Comparator;
import java.util.Collections;

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

    @Transient
    private String owner;
    @Transient
    private int ownerId;
    @Transient
    private BigDecimal totalSpending;
    @Transient
    private int memberCount;

//    @Transient
//    private List<Spending> expenses;

    @JsonManagedReference
    @OneToMany(mappedBy = "room", orphanRemoval = true)
    private Set<MemberRoom> members = new HashSet<>();

//    @JsonManagedReference
//    @OneToMany(mappedBy = "room", orphanRemoval = true)
//    private Set<Spending> expenses = new HashSet<>();

    public String getOwner(){
        for(MemberRoom member : members){
            if(member.getMemberRole() == MemberRole.OWNER){
                return member.getUser().getName();
            }
        }
        return "Owner name not found";

    }

    public  BigDecimal getTotalSpending(){
        BigDecimal sum = BigDecimal.ZERO;
        for(MemberRoom member: members){
            for(Spending spending: member.getSpendings()){
                sum = sum.add(spending.getAmount());
            }
        }
        return sum;
    }

    public int getMemberCount(){
        return  members.size();
    }

    public List<Spending> getExpenses(){
        List<Spending> temp =  members.stream()
                .flatMap(memberRoom -> memberRoom.getSpendings().stream())
                .collect(Collectors.toList());

        Comparator<Spending> descendingOrderComparator = Comparator.comparingLong(Spending::getSpendingId).reversed();

        // Using Collections.sort() for an existing list
        Collections.sort(temp, descendingOrderComparator);

        return temp;
    }

    public int getOwnerId() {
        for(MemberRoom member : members){
            if(member.getMemberRole() == MemberRole.OWNER){
                return member.getUser().getId();
            }
        }
        return -1;
    }
}
