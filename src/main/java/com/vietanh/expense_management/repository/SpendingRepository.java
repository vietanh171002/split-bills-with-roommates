package com.vietanh.expense_management.repository;

import com.vietanh.expense_management.model.Spending;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpendingRepository extends JpaRepository<Spending, Integer> {

}
