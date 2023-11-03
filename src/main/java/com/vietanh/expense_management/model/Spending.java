package com.vietanh.expense_management.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.vietanh.expense_management.model.enumeration.SpendingCategory;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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
    private int amount;
    private LocalDate date;
    @Enumerated(EnumType.STRING)
    private SpendingCategory category;

    @JsonBackReference
    @ManyToOne
    private MemberRoom memberRoom;
}
