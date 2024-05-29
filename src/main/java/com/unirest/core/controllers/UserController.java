package com.unirest.core.controllers;

import com.unirest.core.controllers.base.BaseController;
import com.unirest.core.repositories.NotificationRepository;
import com.unirest.core.repositories.UserRepository;
import com.unirest.core.services.ImageService;
import com.unirest.core.services.UserService;
import com.unirest.core.utils.JWTUtils;
import com.unirest.core.utils.TimedHashMap;
import com.unirest.data.dto.UserDTO;
import com.unirest.data.dto.UserPermitDTO;
import com.unirest.data.dto.UserSearchDTO;
import com.unirest.data.models.User;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping(value = "/user")
public class UserController extends BaseController<User, Long, UserDTO, UserRepository> {
    private final TimedHashMap<String, String> emailCodeMap = new TimedHashMap<>();

    private final NotificationRepository notificationRepository;
    private final UserService userService;
    private final ImageService imageService;


    @Autowired
    public UserController(UserRepository userRepository, NotificationRepository notificationRepository, UserService userService, ImageService imageService) {
        super(userRepository, User.class, UserDTO.class);
        this.notificationRepository = notificationRepository;
        this.userService = userService;
        this.imageService = imageService;
    }

    @GetMapping("/info")
    public ResponseEntity<?> getUser(@RequestHeader("Token") String token) {
        Claims claims = JWTUtils.parse(token);
        if (claims != null) {
            String email = claims.getSubject();
            User user = userService.findByEmail(email);
            if (user != null) {
                return ResponseEntity.ok(new UserDTO(user));
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/permit")
    public ResponseEntity<?> getUser(@RequestParam("Id") Long id) {
        User user = userService.findById(id);
        if (user != null) {
            UserPermitDTO userPermit = new UserPermitDTO(user);
            return ResponseEntity.ok(userPermit);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/check/email")
    public ResponseEntity<String> checkEmail(@RequestParam(name = "email") String email) {
        if (userService.isEmailRegistered(email)) {
            return ResponseEntity.status(202).build();
        }
        return ResponseEntity.status(200).build();
    }

    @GetMapping("/check/token")
    public ResponseEntity<String> checkToken(@RequestHeader("Token") String token) {
        if (userService.isTokenBelongsUser(token)) {
            return ResponseEntity.status(202).build();
        }
        return ResponseEntity.status(200).build();
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestHeader("Email") String email,
                                           @RequestHeader("Password") String /*SHA256*/ password) {
        User user = userService.register(email, password);
        return ResponseEntity.ok(JWTUtils.create(user, user.getSession()).compact());
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestHeader(name = "Username", required = false) String username,
                                        @RequestHeader(name = "Password", required = false) /*SHA256*/ String password,
                                        @RequestHeader(name = "Token", required = false) /*JWT*/ String token) {

        if (token != null && userService.isTokenBelongsUser(token)) {
            Claims claims = JWTUtils.parse(token);
            if (claims != null) {
                User user = userService.findById(claims.get("id", Long.class));
                if (user != null) {
                    if (user.getSubject().equals(claims.getSubject())) {
                        user.getSession().updateDate();
                        userService.saveOrUpdate(user);
                        return ResponseEntity.ok(JWTUtils.create(user, user.getSession()).compact());
                    }
                }
            }
        }

        if (username != null) {
            User user = userService.findByEmail(username);
            if (user == null) {
                user = userService.findByUsername(username);
            }
            if (user != null) {
                if (user.getPassword().equals(password)) {
                    user.getSession().updateDate();
                    userService.saveOrUpdate(user);
                    return ResponseEntity.ok(JWTUtils.create(user, user.getSession()).compact());
                }
            }
        }

        return ResponseEntity.badRequest().build();
    }

    @PostMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateUser(@RequestParam("id") Long userId, @RequestBody User userNew) {
        User userOld = userService.findById(userId);
        if (userOld != null) {
            if (userNew.getUsername() != null) {
                userOld.setUsername(userNew.getUsername());
            }
            if (userNew.getName() != null) {
                userOld.setName(userNew.getName());
            }
            if (userNew.getLastName() != null) {
                userOld.setLastName(userNew.getLastName());
            }
            if (userNew.getSurName() != null) {
                userOld.setSurName(userNew.getSurName());
            }
            if (userNew.getPassword() != null) {
                userOld.setPassword(userNew.getPassword());
            }
            if (userNew.getCourse() != null && userNew.getCourse() != -1) {
                userOld.setCourse(userNew.getCourse());
            }
            if (userNew.getPhoneNumber() != null) {
                userOld.setPhoneNumber(userNew.getPhoneNumber());
            }
            if (userNew.getEmail() != null) {
                userOld.setEmail(userNew.getEmail());
            }

            userService.saveOrUpdate(userOld);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/balance/get")
    public ResponseEntity<?> getBalance(@RequestParam("id") Long id) {
        User user = userService.findById(id);
        if (user != null) {
            return ResponseEntity.ok(user.getBalance());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/balance/add")
    @Transactional
    public void addBalance(@RequestParam("id") Long id, @RequestParam("amount") int amount) {
        User user = userService.findById(id);
        if (user != null) {
            user.setBalance(user.getBalance() + amount);
            userService.saveOrUpdate(user);
        }
    }

    @PostMapping("/balance/draw")
    @Transactional
    public void drawBalance(@RequestParam("id") Long id, @RequestParam("amount") int amount) {
        User user = userService.findById(id);
        if (user != null) {
            user.setBalance(user.getBalance() - amount);
            userService.saveOrUpdate(user);
        }
    }

    @PostMapping("/image/upload")
    public ResponseEntity<?> uploadUser(@RequestParam("id") Long id, @RequestPart("image") MultipartFile multipartFile) {
        User user = userService.findById(id);
        if (user != null) {
            imageService.saveImage("avatars", user.getEmail(), multipartFile);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<?> getAvatar(@PathVariable("id") Long id) {
        User user = userService.findById(id);
        if (user != null) {
            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(imageService.getImageById("avatars", user.getEmail()));
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/email/verify")
    public ResponseEntity<?> verifyEmail(
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "code", required = false) String code) {

        if (email != null && !email.isEmpty()) {
            if (code != null) {
                String codeString = emailCodeMap.get(email);
                if (codeString != null && codeString.equals(code)) {
                    return ResponseEntity.ok().build();
                }
                return ResponseEntity.badRequest().build();
            }

            if (userService.findByEmail(email) != null) {
                String codeString = String.valueOf(getRandomNumbers(1001, 9999));
                emailCodeMap.put(email, codeString, 900_000);
                return ResponseEntity.status(201).build();
            }
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/of")
    public ResponseEntity<?> of(@RequestParam("id") Long roomId) {
        List<User> allByRoomId = repository.findAllByRoomId(roomId);
        if (allByRoomId.isEmpty()) {
            ResponseEntity.notFound().build();
        }
        List<UserDTO> roomUsers = new ArrayList<>();

        for (User user : allByRoomId) {
            roomUsers.add(new UserDTO(user));
        }

        return ResponseEntity.ok(roomUsers);
    }

    @PostMapping("/update/status")
    public ResponseEntity<?> updateActivity(@RequestParam("id") Long userId) {
        User user = userService.findById(userId);
        if (user != null) {
            user.setLastActive(System.currentTimeMillis());
            userService.saveOrUpdate(user);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchUsers(@RequestParam(name = "name", required = false) String name,
                                         @RequestParam(name = "id", required = false) Long dormitoryId) {
        List<User> allUsers = repository.searchByKeyword(name);

        if (allUsers != null && !allUsers.isEmpty()) {
            List<UserSearchDTO> userDTOS = new ArrayList<>();
            if (dormitoryId != null) {
                for (User allUser : allUsers) {
                    if (allUser.getDormitoryId().equals(dormitoryId)) {
                        userDTOS.add(new UserSearchDTO(allUser));
                    }
                }
            } else {
                for (User allUser : allUsers) {
                    userDTOS.add(new UserSearchDTO(allUser));
                }
            }
            return ResponseEntity.ok(userDTOS);
        }
        return ResponseEntity.badRequest().build();
    }

    public int getRandomNumbers(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }


}
