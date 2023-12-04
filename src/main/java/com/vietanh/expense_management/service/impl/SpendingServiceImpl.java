package com.vietanh.expense_management.service.impl;

import com.vietanh.expense_management.dto.request.SpendingDto;
import com.vietanh.expense_management.exception.ActionDeniedException;
import com.vietanh.expense_management.exception.MemberRoomNotFoundException;
import com.vietanh.expense_management.exception.RoomNotFoundException;
import com.vietanh.expense_management.exception.SpendingNotFoundException;
import com.vietanh.expense_management.model.MemberRoom;
import com.vietanh.expense_management.model.Room;
import com.vietanh.expense_management.model.Spending;
import com.vietanh.expense_management.model.User;
import com.vietanh.expense_management.model.enumeration.MemberRole;
import com.vietanh.expense_management.model.enumeration.SpendingCategory;
import com.vietanh.expense_management.repository.MemberRoomRepository;
import com.vietanh.expense_management.repository.RoomRepository;
import com.vietanh.expense_management.repository.SpendingRepository;
import com.vietanh.expense_management.service.SpendingService;
import com.vietanh.expense_management.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
@Transactional
@RequiredArgsConstructor
public class SpendingServiceImpl implements SpendingService {

    private final UserService userService;

    private final RoomRepository roomRepository;

    private final SpendingRepository spendingRepository;

    private final MemberRoomRepository memberRoomRepository;

    //define the math context for division of BigDecimal
    MathContext mathContext = new MathContext(10, RoundingMode.HALF_UP);

    //add spending
    @Override
    public void createSpending(int RoomId, String detail, BigDecimal amount, LocalDate date, SpendingCategory category) {
        User user = userService.getUserFromSecurity();

        Room room = roomRepository.findByRoomId(RoomId).orElseThrow(
                () -> new RoomNotFoundException("Room not found")
        );

        MemberRoom memberRoom = memberRoomRepository.findByRoomAndUser(room, user).orElseThrow(
                () -> new MemberRoomNotFoundException("You aren't member of this room")
        );

        roomRepository.save(room);

        Spending spending = Spending.builder()
                .detail(detail)
                .amount(amount)
                .date(date)
                .category(category)
                .memberRoom(memberRoom)
                .build();

        spendingRepository.save(spending);
        memberRoom.getSpendings().add(spending);

        //divide money
        BigDecimal amountPerMember = amount.divide(BigDecimal.valueOf(room.getMembers().size()), mathContext);

        room.getMembers().forEach(mem -> mem.setBalance(mem.getBalance().subtract(amountPerMember)));
        memberRoom.setBalance(memberRoom.getBalance().add(amount));
        memberRoomRepository.saveAll(room.getMembers());
    }

    //edit spending
    @Override
    public void editSpending(int spendingId, SpendingDto editDto) {
        //get logged-in user 
        User user = userService.getUserFromSecurity();

        //get spending
        Spending spending = spendingRepository.findById(spendingId).orElseThrow(
                () -> new SpendingNotFoundException("Spending not found")
        );

        //ensure logged-in user is spending creator
        if (!spending.getMemberRoom().getUser().equals(user)) {
            throw new ActionDeniedException("Only spending creator could edit spending");
        }

        //recalculate balance of every member and totalSpending of room if spending amount changes
        if (spending.getAmount() != editDto.getAmount()) {
            MemberRoom memberRoom = spending.getMemberRoom();
            Room room = memberRoom.getRoom();
            int memberCount = room.getMembers().size();

            BigDecimal oldAmountPerMember = spending.getAmount().divide(BigDecimal.valueOf(memberCount), mathContext);
            room.getMembers().forEach(
                    member -> member.setBalance(member.getBalance().add(oldAmountPerMember))
            );
            memberRoom.setBalance(memberRoom.getBalance().subtract(spending.getAmount()) );

            BigDecimal newAmountPerMember = editDto.getAmount().divide(BigDecimal.valueOf(memberCount), mathContext);
            room.getMembers().forEach(
                    member -> member.setBalance(member.getBalance().subtract(newAmountPerMember))
            );

            memberRoom.setBalance(memberRoom.getBalance().add(editDto.getAmount()));


        }

        //edit spending 
        spending.setAmount(editDto.getAmount());
        spending.setCategory(editDto.getCategory());
        spending.setDate(editDto.getDate());
        spending.setDetail(editDto.getDetail());

    }

    //only member of the room containing the spending could get that spending
    @Override
    public Spending getSpending(int spendingId) {
        //get logged-in user 
        User user = userService.getUserFromSecurity();

        //get spending
        Spending spending = spendingRepository.findById(spendingId).orElseThrow(
                () -> new SpendingNotFoundException("Spending not found")
        );

        //ensure that logged-in user is member of the room having this spending
        Room room = spending.getMemberRoom().getRoom();
        if (!memberRoomRepository.existsByRoomAndUser(room, user)) {
            throw new MemberRoomNotFoundException("This spending doesn't belong to any room of you, so you couldn't see it");
        }

        return spending;

    }

    //only roomOwner and spending creator could delete that spending 
    @Override
    public void deleteSpending(int spendingId) {
        //get logged-in user 
        User user = userService.getUserFromSecurity();

        //get spending
        Spending spending = spendingRepository.findById(spendingId).orElseThrow(
                () -> new SpendingNotFoundException("Spending not found")
        );

        // get room that has this spending 
        Room room = spending.getMemberRoom().getRoom();

        //get memberRoom of logged-in user and room
        MemberRoom memberRoom = memberRoomRepository.findByRoomAndUser(room, user).orElseThrow(
                () -> new MemberRoomNotFoundException("Spending not found in your rooms")
        );

        //ensure that only roomOwner and spending creator could delete that spending 
        if (!spending.getMemberRoom().getUser().equals(user) && !memberRoom.getMemberRole().equals(MemberRole.OWNER)) {
            throw new ActionDeniedException("Only spending creator and room owner could delete spending");
        }

        //recalculate balances of members and totalSpending because of deletion of spending
        int memberCount = room.getMembers().size();

        BigDecimal oldAmountPerMember = spending.getAmount().divide(BigDecimal.valueOf(memberCount), mathContext);
        room.getMembers().forEach(
                member -> member.setBalance(member.getBalance().add(oldAmountPerMember))
        );
        memberRoom.setBalance(memberRoom.getBalance().subtract(spending.getAmount()));


        //delete spending
        spendingRepository.delete(spending);
    }
}
