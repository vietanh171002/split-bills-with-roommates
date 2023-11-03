package com.vietanh.expense_management.repository;

import java.util.HashSet;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.vietanh.expense_management.model.enumeration.MemberRole;
import com.vietanh.expense_management.model.MemberRoom;
import com.vietanh.expense_management.model.Room;
import com.vietanh.expense_management.model.Spending;
import com.vietanh.expense_management.model.User;
import com.vietanh.expense_management.model.test_model.Child;
import com.vietanh.expense_management.model.test_model.Parent;

@DataJpaTest
public class RepoTest {
     
    @Autowired
    private MemberRoomRepository memberRoomRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private ChildRepository childRepository;
    @Autowired
    private ParentRepository parentRepository; 
    @Autowired
    private SpendingRepository spendingRepository;

    // @Test
    // public void RepoTest1(){
    //     User user = new User(); 
    //     Room room = new Room(); 
    //     userRepository.save(user); 
    //     roomRepository.save(room);

    //     MemberRoom memberRoom = MemberRoom.builder()
    //                                         .room(room)
    //                                         // .user(user)
    //                                         .build(); 
    //     // user.setRooms(new HashSet<>());
    //     room.setMembers(new HashSet<>());

    //     // user.getRooms().add(memberRoom); 
    //     room.getMembers().add(memberRoom); 

    //     memberRoomRepository.save(memberRoom); 
         

    //     room.getMembers().remove(memberRoom); 
    //     roomRepository.save(room); 

    //     assertThat(memberRoomRepository.findAll()).isNull();

    // }
    
    //test cascadeType.REMOVE of @OneToMany
    @Test
    public void repoTest2(){
        Parent parent = new Parent(); 
        Child child1 = new Child(); 
        Child child2 = new Child();
         
        parentRepository.save(parent); 
        child1.setParent(parent);
        child2.setParent(parent);
        childRepository.save(child1);
        childRepository.save(child2);

        assertThat(childRepository.count()).isEqualTo(2);

        parent.setChilds(new HashSet<>());
        parent.getChilds().add(child1); 
        parent.getChilds().add(child2);
        parentRepository.save(parent); 

        // parent.getChilds().remove(child2); 
        // parentRepository.save(parent); 
        parentRepository.delete(parent);

        assertThat(childRepository.count()).isEqualTo(0);
    }

    //test cascadeType.REMOVE of @ManyToOne 
    @Test
    // @Transactional
    public void repoTest3(){
        Parent parent = new Parent(); 
        Child child1 = new Child(); 
        Child child2 = new Child();

        assertThat(childRepository.count()).isEqualTo(0);
         
        parentRepository.save(parent); 
        
        child1.setParent(parent);
        child2.setParent(parent);

        childRepository.save(child1);
        childRepository.save(child2);

        assertThat(childRepository.count()).isEqualTo(2);
        assertThat(parentRepository.count()).isEqualTo(1);

        parent.setChilds(new HashSet<>());
        parent.getChilds().add(child1); 
        parent.getChilds().add(child2);

        parentRepository.save(parent);

        // parent.getChilds().forEach(
        //     child -> child.setParent(null)
        // );

        for(Child child : parent.getChilds()){
            child.setParent(null);
        }

        parentRepository.delete(parent);

        assertThat(childRepository.count()).isEqualTo(2);
        assertThat(parentRepository.count()).isEqualTo(0);
    }

    @Test
    public void repoTest4(){
        User user = User.builder().id(1).name("vanh").rooms(new HashSet<>()).build(); 
        String userName = user.getName();

        userRepository.save(user);

        Room room = Room.builder()
                        .members(new HashSet<>())
                        .roomName("phong 2")
                        .owner(userName)
                        .totalSpending(0)
                        .build();

        roomRepository.save(room);

        MemberRoom memberRoom = MemberRoom.builder()
                                            .balance(0)
                                            .spentAmount(0)
                                            .room(room)
                                            .user(user)
                                            .memberRole(MemberRole.OWNER)
                                            .spendings(new HashSet<>())
                                            .build();

        memberRoomRepository.save(memberRoom);

        room.getMembers().add(memberRoom);
        user.getRooms().add(memberRoom);

        Spending spending = Spending.builder()
                                    .detail("detail")
                                    .amount(100)
                                    .memberRoom(memberRoom)
                                    .build();
        
        spendingRepository.save(spending);
        memberRoom.getSpendings().add(spending);

        userRepository.save(user);
        roomRepository.save(room);
        memberRoomRepository.save(memberRoom);

        memberRoomRepository.delete(memberRoom);

    }


}
