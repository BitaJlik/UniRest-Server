package com.unirest.core.controllers;

import com.unirest.core.controllers.base.BaseController;
import com.unirest.data.models.User;
import com.unirest.data.models.Washer;
import com.unirest.data.dto.WasherDTO;
import com.unirest.core.repositories.UserRepository;
import com.unirest.core.repositories.WasherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/washer")
public class WasherController extends BaseController<Washer, Long, WasherDTO, WasherRepository> {

    private final UserRepository userRepository;

    @Autowired
    public WasherController(WasherRepository repository, UserRepository userRepository) {
        super(repository, Washer.class, WasherDTO.class);
        this.userRepository = userRepository;
    }

    @PostMapping("/busy")
    public ResponseEntity<?> updateWasherBusy(@RequestParam("id") Long id, @RequestParam("userId") Long userId, @RequestParam("time") int minutes) {
        Optional<User> userById = userRepository.findById(userId);
        Optional<Washer> byId = repository.findById(id);
        if (byId.isPresent() && userById.isPresent()) {
            Washer washer = byId.get();
            washer.setBusy(true);
            washer.setBusyTo(System.currentTimeMillis() + (long) minutes * 60 * 1_000);
            washer.setLastUse(System.currentTimeMillis());
            washer.setUser(userById.get());
            repository.save(washer);
            return ResponseEntity.status(202).build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/of")
    public ResponseEntity<?> getWashersList(@RequestParam("id") Long id) {
        List<Washer> washers = repository.findAllByFloorId(id);
        if (washers != null && !washers.isEmpty()) {
            List<WasherDTO> washersList = new ArrayList<>();
            for (Washer washer : washers) {
                washersList.add(new WasherDTO(washer));
            }
            return ResponseEntity.ok(washersList);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/free")
    public ResponseEntity<?> updateWasherFree(@RequestParam("id") Long id, @RequestParam("userId") Long userId) {
        Optional<Washer> byId = repository.findById(id);
        if (byId.isPresent()) {
            Washer washer = byId.get();
            if (washer.getUser().getId().equals(userId)) {
                washer.setBusy(false);
                washer.setUser(null);
                repository.save(washer);
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.notFound().build();
    }

}
