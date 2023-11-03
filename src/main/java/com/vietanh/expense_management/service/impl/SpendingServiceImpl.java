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

import java.time.LocalDate;

@Service
@Transactional
@RequiredArgsConstructor
public class SpendingServiceImpl implements SpendingService {

    private final UserService userService;

    private final RoomRepository roomRepository;

    private final SpendingRepository spendingRepository;

    private final MemberRoomRepository memberRoomRepository;

    @Override
    public Room createSpending(int RoomId, String detail, int amount, LocalDate date, SpendingCategory category) {
        User user = userService.getUserFromSecurity();

        Room room = roomRepository.findByRoomId(RoomId).orElseThrow(
                RoomNotFoundException::new
        );

        MemberRoom memberRoom = memberRoomRepository.findByRoomAndUser(room, user).orElseThrow(
                () -> new MemberRoomNotFoundException("You aren't member of this room")
        );

        room.setTotalSpending(room.getTotalSpending() + amount);
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

        int amountPerMember = amount / room.getMembers().size();
        room.getMembers().forEach(mem -> mem.setBalance(mem.getBalance() - amountPerMember));
        memberRoom.setBalance(memberRoom.getBalance() + amount);
        memberRoomRepository.saveAll(room.getMembers());
        return room;
    }

    @Override
    public Spending editSpending(int spendingId, SpendingDto editDto) {
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

            int oldAmountPerMember = spending.getAmount() / memberCount;
            room.getMembers().forEach(
                    member -> member.setBalance(member.getBalance() + oldAmountPerMember)
            );
            memberRoom.setBalance(memberRoom.getBalance() - spending.getAmount());

            int newAmountPerMember = editDto.getAmount() / memberCount;
            room.getMembers().forEach(
                    member -> member.setBalance(member.getBalance() - newAmountPerMember)
            );

            memberRoom.setBalance(memberRoom.getBalance() + editDto.getAmount());

            //change room's totalSpending
            room.setTotalSpending(room.getTotalSpending() - spending.getAmount() + editDto.getAmount());
        }

        //edit spending 
        spending.setAmount(editDto.getAmount());
        spending.setCategory(editDto.getCategory());
        spending.setDate(editDto.getDate());
        spending.setDetail(editDto.getDetail());

        return spending;
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

        int oldAmountPerMember = spending.getAmount() / memberCount;
        room.getMembers().forEach(
                member -> member.setBalance(member.getBalance() + oldAmountPerMember)
        );
        memberRoom.setBalance(memberRoom.getBalance() - spending.getAmount());

        room.setTotalSpending(room.getTotalSpending() - spending.getAmount());

        //delete spending
        spendingRepository.delete(spending);
    }
}
