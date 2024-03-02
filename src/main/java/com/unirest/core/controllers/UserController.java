package com.unirest.core.controllers;

import com.unirest.core.controllers.base.BaseController;
import com.unirest.core.models.User;
import com.unirest.core.repositories.UserRepository;
import com.unirest.core.services.UserService;
import com.unirest.core.utils.JWTUtils;
import com.unirest.core.utils.JsonUtils;
import io.jsonwebtoken.Claims;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/user")
public class UserController extends BaseController<User, Long, User, UserRepository> {

    @Autowired
    private UserService userService;

    public UserController(UserRepository userRepository) {
        super(userRepository, User.class, User.class);
    }

    @GetMapping("/checkemail")
    public ResponseEntity<String> checkEmail(@RequestParam(name = "email") String email) {
        if (userService.isEmailRegistered(email)) {
            return ResponseEntity.badRequest().body("Email already registered");
        }
        return ResponseEntity.ok().body("Email not registered");
    }

    @GetMapping("/checktoken")
    public ResponseEntity<String> checkToken(@RequestHeader("Token") String token) {
        if (userService.isTokenBelongsUser(token)) {
            return ResponseEntity.status(202).build();
        }
        return ResponseEntity.status(HttpStatusCode.valueOf(403)).build();
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody String body,
                                           @RequestHeader("Password") String /*SHA256*/ password) {
        JSONObject object = new JSONObject(body);
        if (JsonUtils.hasKeys(object, "username", "email", "password")) {
            userService.register(object.getString("username"), object.getString("email"), password);
            User user = userService.findByUsername(object.getString("username"));
            if (user != null) {
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestHeader(name = "Username", required = false) String username,
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
            User user = userService.findByUsername(username);
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
        }
    }

    @PostMapping("/balance/draw")
    @Transactional
    public void drawBalance(@RequestParam("id") Long id, @RequestParam("amount") int amount) {
        User user = userService.findById(id);
        if (user != null) {
            user.setBalance(user.getBalance() - amount);
        }
    }


}
