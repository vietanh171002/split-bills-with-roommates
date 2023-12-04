package com.vietanh.expense_management.controller;

import com.vietanh.expense_management.dto.request.SpendingDto;
import com.vietanh.expense_management.model.Room;
import com.vietanh.expense_management.model.Spending;
import com.vietanh.expense_management.service.SpendingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/spending")
@RequiredArgsConstructor
public class SpendingController {

    private final SpendingService spendingService;

    //create spending
    @PostMapping(value = "create/{roomId}")
    public ResponseEntity<?> createSpending(@RequestBody SpendingDto spendingDto, @PathVariable int roomId) {
//        if(spendingDto.getDetail().isBlank() || spendingDto.getDetail().isBlank() )
        spendingService.createSpending(
                roomId, spendingDto.getDetail(), spendingDto.getAmount(), spendingDto.getDate(), spendingDto.getCategory());
        return ResponseEntity.noContent().build();
    }

    //edit spending
    @PutMapping(value = "/{spendingId}/edit")
    public ResponseEntity<?> editSpending(@PathVariable int spendingId, @RequestBody SpendingDto editDto) {
      spendingService.editSpending(spendingId, editDto);
        return ResponseEntity.noContent().build();
    }

    //get spending
    @GetMapping(value = "/{spendingId}/get-spending")
    public ResponseEntity<?> getSpending(@PathVariable int spendingId) {
        Spending spending = spendingService.getSpending(spendingId);
        return ResponseEntity.ok().body(spending);
    }

    //delete spending
    @DeleteMapping(value = "/{spendingId}/delete-spending")
    public ResponseEntity<?> deleteSpending(@PathVariable int spendingId) {
        spendingService.deleteSpending(spendingId);
        return ResponseEntity.ok().body("Delete spending successfully");
    }

}
