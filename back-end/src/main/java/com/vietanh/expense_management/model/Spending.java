package com.vietanh.expense_management.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.vietanh.expense_management.model.enumeration.SpendingCategory;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "spending")
public class Spending {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int spendingId;
    private String detail;
    private BigDecimal amount;
    private LocalDate date;

    //spender info
    @Transient
    private String spender;
    @Transient
    private int spenderId;
    public  String getSpender(){
        return memberRoom.getUser().getName();
    }

    public int getSpenderId() {
        return  memberRoom.getUser().getId();
    }

    @Enumerated(EnumType.STRING)
    private SpendingCategory category;

    @JsonBackReference
    @ManyToOne
    private MemberRoom memberRoom;

//    @JsonBackReference
//    @ManyToOne
//    private Room room;
//
//    @JsonBackReference
//    @ManyToOne
//    private User user;

}
