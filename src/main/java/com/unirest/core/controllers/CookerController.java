package com.unirest.core.controllers;

import com.unirest.core.controllers.base.BaseController;
import com.unirest.core.repositories.UserRepository;
import com.unirest.data.dto.CookerDTO;
import com.unirest.data.dto.WasherDTO;
import com.unirest.data.models.Cooker;
import com.unirest.core.repositories.CookersRepository;
import com.unirest.data.models.Floor;
import com.unirest.data.models.User;
import com.unirest.data.models.Washer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cooker")
public class CookerController extends BaseController<Cooker, Long, CookerDTO, CookersRepository> {
    private final UserRepository userRepository;

    public CookerController(CookersRepository repository, UserRepository userRepository) {
        super(repository, Cooker.class, CookerDTO.class);
        this.userRepository = userRepository;
    }

    @GetMapping("/of")
    public ResponseEntity<?> getWashersList(@RequestParam("id") Long id) {
        List<Cooker> cookers = repository.findAllByFloorId(id);
        if (cookers != null && !cookers.isEmpty()) {
            List<CookerDTO> washersList = new ArrayList<>();
            for (Cooker cooker : cookers) {
                if (cooker.isBusy()) {
                    if (cooker.getBusyTo() < System.currentTimeMillis()) {
                        cooker.setUser(null);
                        cooker.setBusy(false);
                        repository.save(cooker);
                    }
                }
                washersList.add(new CookerDTO(cooker));
            }
            return ResponseEntity.ok(washersList);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/busy")
    public ResponseEntity<?> updateWasherBusy(@RequestParam("id") Long id, @RequestParam("userId") Long userId, @RequestParam("time") int minutes) {
        Optional<User> userById = userRepository.findById(userId);
        Optional<Cooker> byId = repository.findById(id);
        if (byId.isPresent() && userById.isPresent()) {
            Cooker cooker = byId.get();
            cooker.setBusy(true);
            cooker.setBusyTo(System.currentTimeMillis() + (long) minutes * 60 * 1_000);
            cooker.setLastUse(System.currentTimeMillis());
            cooker.setUser(userById.get());
            repository.save(cooker);
            return ResponseEntity.status(202).build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/free")
    public ResponseEntity<?> updateWasherFree(@RequestParam("id") Long id, @RequestParam("userId") Long userId) {
        Optional<Cooker> byId = repository.findById(id);
        if (byId.isPresent()) {
            Cooker cooker = byId.get();
            if (cooker.getUser().getId().equals(userId)) {
                cooker.setBusy(false);
                cooker.setUser(null);
                repository.save(cooker);
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/admin/update")
    public ResponseEntity<?> uploadWasher(@RequestParam("remove") Boolean isRemove, @RequestBody CookerDTO cookerDTO) {
        if (isRemove != null) {
            if (isRemove) {
                if (cookerDTO.getId() != null) {
                    repository.deleteById(cookerDTO.getId());
                    return ResponseEntity.ok().build();
                }
            } else {
                if (cookerDTO.getFloor() != null) {
                    Floor floor = new Floor();
                    floor.setId(cookerDTO.getFloor());
                    Cooker cooker = new Cooker();
                    cooker.setFloor(floor);
                    repository.save(cooker);
                    return ResponseEntity.ok().build();
                }
            }
        }
        return ResponseEntity.badRequest().build();
    }

}
