package com.unirest.core.controllers;

import com.unirest.core.repositories.*;
import com.unirest.data.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("/test")
// TODO: 07.05.2024 REMOVE IT BEFORE POST IN PRODUCTION
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
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private UserRolesRepository userRolesRepository;
    @Autowired
    private NotificationRepository notificationRepository;

    private static final String[] FIRST_NAMES = {"John", "Emma", "Michael", "Sophia", "William", "Olivia", "James", "Ava", "Alexander", "Isabella"};
    private static final String[] LAST_NAMES = {"Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller", "Wilson", "Moore", "Taylor"};
    private static final String[] DOMAINS = {"gmail.com", "yahoo.com", "hotmail.com", "outlook.com"};
    private static final String[] PASSWORDS = {"password1", "password2", "password3", "password4", "password5"};

    private static final String[] ROLES = {"commandant", "helper", "dorm_supervisor", "floor_supervisor", "other", "default"};
    private static final Long[] expires = {1622870400L, 1654406400L, 1686038400L, 1717574400L};

    @GetMapping("/init")

    public ResponseEntity<List<Dormitory>> test() {
        List<Dormitory> dormitories = new ArrayList<>();

        for (int i = 1; i < 2; i++) {
            Dormitory dormitory = new Dormitory();

            dormitoryRepository.saveAndFlush(dormitory);

            for (int s = 0; s < 5; s++) {
                UserRole userRole = new UserRole();
                userRole.setDormitory(dormitory);
                userRole.setName(ROLES[s]);
                userRole.setLevel(s);
                userRolesRepository.save(userRole);
            }

            dormitory.setName("Гуртожиток №" + i);
            dormitory.setAddress("Вулия №" + i);
            dormitory.setHasElevator(Math.random() * 10 > 6);
            dormitory.setCookerType(random(CookerType.values()));
            User commandant = createUser();
            commandant.setRole((userRolesRepository.findAll().get(0)));
            dormitory.setCommandant(commandant);
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
                        user.setPayments(createPayments(dormitory, user));
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
        Random random = new Random();
        int minDay = (int) LocalDate.of(2021, 1, 1).toEpochDay();
        int maxDay = (int) LocalDate.of(2024, 1, 1).toEpochDay();

        List<User> all = userRepository.findAll();
        for (User user : all) {
            Notification notification = new Notification();
            notification.setContent(UUID.randomUUID().toString().replace("-", ""));
            notification.setTitle(UUID.randomUUID().toString().replace("-", ""));
            long randomDay = minDay + random.nextInt(maxDay - minDay);
            notification.setDate(randomDay);
            if (Math.random() * 10 > 5) {
                notification.setReceiver(user);
                notification.setSender(random(all.toArray(new User[]{})));
            } else {
                notification.setSender(user);
                notification.setReceiver(random(all.toArray(new User[]{})));
            }
            notificationRepository.save(notification);
        }

        return ResponseEntity.ok(dormitories);
    }

    private List<Payment> createPayments(Dormitory dormitory, User user) {
        List<Payment> list = new ArrayList<>();
        for (int i = 0; i < Math.random() * 6; i++) {
            Payment payment = new Payment();
            payment.setDate((long) (System.currentTimeMillis() - Math.random() * System.currentTimeMillis()));
            payment.setBalance((int) (500 + Math.random() * 500));
            payment.setUser(user);
            payment.setDormitory(dormitory);
            list.add(paymentRepository.saveAndFlush(payment));
        }
        return list;
    }

    @GetMapping("/get")
    public ResponseEntity<Dormitory> get() {
        Dormitory body = dormitoryRepository.findAll().get(0);
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
            request.setType(Request.RequestType.values()[(int) (Math.random() * Request.RequestType.values().length)]);
            request.setUser(user);
            list.add(requestRepository.save(request));
        }

        return list;
    }

    private static int i;

    public User createUser() {
        User user = new User();
        user.setName(random(FIRST_NAMES));
        user.setLastName(random(LAST_NAMES));
        user.setSurName(random(FIRST_NAMES) + "`s");
        user.setPassword(encryptSHA256(random(PASSWORDS)));
        user.setEmail(generateEmail(user.getName() + ++i, user.getLastName()));
        user.setCourse((int) (1 + Math.random() * 3));
        user.setSession(createSession());
        user.setBalance((int) (Math.random() * 1000));
        user.setPhoneNumber(generatePhoneNumber());
        user.setExpire(random(expires) * 1000);
        user.setRole(userRolesRepository.findAll().get(5));

        return userRepository.saveAndFlush(user);
    }


    private String generateEmail(String firstName, String lastName) {
        Random random = new Random();
        String domain = DOMAINS[random.nextInt(DOMAINS.length)];
        return firstName.toLowerCase() + "." + lastName.toLowerCase() + "@" + domain;
    }

    public static <T> T random(T[] list) {
        return list[(int) (Math.random() * list.length)];
    }

    private String encryptSHA256(String str) {
        String ps;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(str.getBytes());
            ps = transformHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
        return ps;
    }

    private String transformHex(byte[] bts) {
        StringBuilder des = new StringBuilder();
        String tmp;
        for (byte bt : bts) {
            tmp = (Integer.toHexString(bt & 0xFF));
            if (tmp.length() == 1) {
                des.append("0");
            }
            des.append(tmp);
        }
        return des.toString();
    }

    public String generatePhoneNumber() {
        String countryCode = "38";
        String[] operatorCodes = {"66", "67", "68", "97", "98", "50"};
        Random random = new Random();
        StringBuilder phoneNumber = new StringBuilder(countryCode + "0" + random(operatorCodes));

        for (int i = 0; i < 7; i++) {
            phoneNumber.append(random.nextInt(10));
        }

        return phoneNumber.toString();
    }


}
