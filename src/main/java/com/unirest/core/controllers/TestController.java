package com.unirest.core.controllers;

import com.unirest.core.models.*;
import com.unirest.core.repositories.*;
import jakarta.transaction.TransactionScoped;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DormitoryRepository dormitoryRepository;
    @Autowired
    private FloorRepository floorRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private CookersRepository cookersRepository;
    @Autowired
    private RequestRepository requestRepository;

    private static final String[] FIRST_NAMES = {"John", "Emma", "Michael", "Sophia", "William", "Olivia", "James", "Ava", "Alexander", "Isabella"};
    private static final String[] LAST_NAMES = {"Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller", "Wilson", "Moore", "Taylor"};
    private static final String[] DOMAINS = {"gmail.com", "yahoo.com", "hotmail.com", "outlook.com"};
    private static final String[] PASSWORDS = {"password1", "password2", "password3", "password4", "password5"};

    @GetMapping("/init")
    public ResponseEntity<List<Dormitory>> test() {
        System.out.println(System.currentTimeMillis());
        List<Dormitory> dormitories = new ArrayList<>();

        for (int i = 1; i < 2; i++) {
            Dormitory dormitory = new Dormitory();
            dormitory.setName("Гуртожиток №" + i);
            dormitory.setAddress("Вулия №" + i);
            dormitory.setHasElevator(Math.random() * 10 > 6);
            dormitory.setCommandant(createUser());
            dormitoryRepository.saveAndFlush(dormitory);

            List<Floor> floors = new ArrayList<>();
            for (int j = 1; j < 8; j++) {
                Floor floor = createFloor(j);
                List<Room> rooms = new ArrayList<>();
                for (int k = 1; k < 16; k++) {
                    Room room = createRoom(j, k);
                    List<User> users = new ArrayList<>();
                    for (int i1 = 0; i1 < room.getAvailableBeds(); i1++) {
                        User user = createUser();
                        user.setRoom(room);
                        user.setRequests(createRequests(dormitory, user));
                        users.add(user);
                    }
                    room.setUsers(users);
                    room.setFloor(floor);
                    rooms.add(room);
                }
                List<Cooker> cookers = new ArrayList<>();
                for (int k = 0; k < 4; k++) {
                    Cooker cooker = createCooker();
                    cooker.setFloor(floor);
                    cookers.add(cooker);
                }
                floor.setCookers(cookers);
                floor.setRooms(rooms);
                floor.setDormitory(dormitory);
                floors.add(floor);
            }

            dormitory.setFloors(floors);
            dormitories.add(dormitory);
        }

        dormitoryRepository.saveAll(dormitories);

        return ResponseEntity.ok(dormitories);
    }

    @GetMapping("/get")
    public ResponseEntity<Dormitory> get() {
        Dormitory body = dormitoryRepository.findAll().get(0);
        System.out.println(body);
        return ResponseEntity.ok(body);
    }

    public Floor createFloor(int floorNumber) {
        Floor floor = new Floor();
        floor.setFloorNumber(floorNumber);
        return floorRepository.saveAndFlush(floor);
    }

    public Cooker createCooker() {
        Cooker cooker = new Cooker();
        cooker.setLastUse((long) (System.currentTimeMillis() - Math.random() * System.currentTimeMillis()));
        cooker.setBusy(Math.random() * 10 > 8);
        return cookersRepository.saveAndFlush(cooker);
    }

    public Room createRoom(int floor, int number) {
        Room room = new Room();
        room.setRoomNumber(floor * 100 + number);
        room.setAvailableBeds((int) (2 + Math.random() * 2));
        return roomRepository.saveAndFlush(room);
    }

    public Session createSession() {
        Session session = new Session();
        session.updateDate();
        return sessionRepository.saveAndFlush(session);
    }

    private List<Request> createRequests(Dormitory dormitory, User user) {
        List<Request> list = new ArrayList<>();
        Random random = new Random();
        int minDay = (int) LocalDate.of(2021, 1, 1).toEpochDay();
        int maxDay = (int) LocalDate.of(2024, 1, 1).toEpochDay();

        for (int i = 0; i < 1 + Math.random() * 4; i++) {
            Request request = new Request();
            long randomDay = minDay + random.nextInt(maxDay - minDay);
            request.setDate(randomDay);
            request.setHeader(UUID.randomUUID().toString().replace("-", ""));
            request.setDormitory(dormitory);
            request.setUser(user);
            requestRepository.save(request);
            list.add(request);
        }

        return list;
    }

    public User createUser() {
        User user = new User();
        user.setName(random(FIRST_NAMES));
        user.setLastName(random(LAST_NAMES));
        user.setSurName(random(FIRST_NAMES) + "`s");
        user.setPassword(random(PASSWORDS));
        user.setEmail(generateEmail(user.getName(), user.getLastName()));
        user.setUuid(UUID.randomUUID().toString());
        user.setCourse((int) (1 + Math.random() * 3));
        user.setSession(createSession());
        return userRepository.saveAndFlush(user);
    }


    private String generateEmail(String firstName, String lastName) {
        Random random = new Random();
        String domain = DOMAINS[random.nextInt(DOMAINS.length)];
        return firstName.toLowerCase() + "." + lastName.toLowerCase() + "@" + domain;
    }

    public <T> T random(T[] list) {
        return list[(int) (Math.random() * list.length)];
    }


}
