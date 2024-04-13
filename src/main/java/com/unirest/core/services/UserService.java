package com.unirest.core.services;

import com.unirest.core.models.Session;
import com.unirest.core.models.User;
import com.unirest.core.repositories.SessionRepository;
import com.unirest.core.repositories.UserRepository;
import com.unirest.core.utils.JWTUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    SessionRepository sessionRepository;

    public User saveOrUpdate(User user) {
        return userRepository.save(user);
    }

    public boolean isEmailRegistered(String email) {
        return userRepository.existsByEmail(email);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public boolean isTokenBelongsUser(String token) {
        if (token != null) {
            Claims claims = JWTUtils.parse(token);
            if (claims != null) {
                Long id = claims.get("id", Long.class);
                Optional<User> optionalUser = userRepository.findById(id);
                if (optionalUser.isPresent()) {
                    User user = optionalUser.get();
                    if (user.getSubject().equals(claims.getSubject())) {
                        if (user.getSession().getExpire() == claims.getExpiration().getTime() / 1_000) {
                            return claims.getExpiration().getTime() > System.currentTimeMillis();
                        }
                    }
                }
            }
        }
        return false;
    }

    public User register(String email, String password) {
        User user = new User();

        user.setEmail(email);
        user.setPassword(password);

        Session session = new Session();
        session.updateDate();

        user.setSession(sessionRepository.save(session));

        return userRepository.save(user);
    }

    public void onUpdateActive(User user) {
        user.setLastActive(System.currentTimeMillis());
        userRepository.save(user);
    }

}
