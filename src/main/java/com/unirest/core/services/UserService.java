package com.unirest.core.services;

import com.unirest.core.models.User;
import com.unirest.core.repositories.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public void register(@RequestBody String body) {
        JSONObject object = new JSONObject(body);
        User user = new User();
        user.setUsername(object.getString("username"));
        userRepository.save(user);
    }

}
