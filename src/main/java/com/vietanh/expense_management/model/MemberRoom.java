package com.vietanh.expense_management.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.vietanh.expense_management.model.composite_key.MemberRoomId;
import com.vietanh.expense_management.model.enumeration.MemberRole;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
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
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    //room info

    @Transient
    private String roomName;

    @Transient
    private int roomId;

    //member info
    @Transient
    private String memberName;

    @Transient
    private int userId;

    @Transient
    private BigDecimal amountSpent;

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

    public BigDecimal getAmountSpent(){
        BigDecimal sum = BigDecimal.ZERO;
        for(Spending spending: spendings){
            sum = sum.add(spending.getAmount());
        }
        return sum;
    }

    public String getRoomName(){
        return room.getRoomName();
    }

    public int getRoomId(){
        return room.getRoomId();
    }

    public String getMemberName(){
        return user.getName();
    }

    public int getUserId(){
        return user.getId();
    }

}
