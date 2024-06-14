package com.unirest.core.controllers;

import com.unirest.core.controllers.base.BaseController;
import com.unirest.core.repositories.DormitoryRepository;
import com.unirest.core.repositories.PaidRepository;
import com.unirest.core.repositories.UserRepository;
import com.unirest.data.dto.PaidDTO;
import com.unirest.data.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequestMapping("/paid")
@RestController
public class PaidController extends BaseController<Paid, Long, PaidDTO, PaidRepository> {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DormitoryRepository dormitoryRepository;

    public PaidController(PaidRepository repository) {
        super(repository, Paid.class, PaidDTO.class);
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendPaid(@RequestBody PaidDTO paidDTO) {
        if (paidDTO != null) {
            Optional<Dormitory> byId = dormitoryRepository.findById(paidDTO.getDormitoryId());
            if (byId.isPresent()) {
                Dormitory dormitory = byId.get();

                Paid paid = new Paid();
                paid.setDormitory(dormitory);
                paid.setBalance(paid.getBalance());
                paid.setDate(paidDTO.getDate());
                repository.save(paid);

                List<User> allByDormitoryId = new ArrayList<>();
                for (Floor floor : dormitory.getFloors()) {
                    for (Room room : floor.getRooms()) {
                        allByDormitoryId.addAll(room.getUsers());
                    }
                }

                for (User user : allByDormitoryId) {
                    if (user.isTakePaid()) {
                        user.setBalance(user.getBalance() - paid.getBalance());
                        userRepository.save(user);
                    }
                }
                return ResponseEntity.ok().build();
            }


        }
        return ResponseEntity.badRequest().build();
    }
}
