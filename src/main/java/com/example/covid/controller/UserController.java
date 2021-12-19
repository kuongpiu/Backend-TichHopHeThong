package com.example.covid.controller;

import com.example.covid.entity.User;
import com.example.covid.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public User getUserInfo() {
        return userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @PutMapping
    public User updateUserInfo(@RequestBody Map<String, String> infoUpdated) {
        User oldUser = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        oldUser.setAddress(infoUpdated.get("address"));
        try {
            oldUser.setDob(Date.valueOf(infoUpdated.get("dob")));
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
        oldUser.setGender(infoUpdated.get("gender"));
        oldUser.setName(infoUpdated.get("name"));
        oldUser.setPhoneNumber(infoUpdated.get("phoneNumber"));
        oldUser.setTelegramUsername(infoUpdated.get("telegramUsername"));
        oldUser.setTelegramUid(infoUpdated.get("telegramUid"));
        return userRepository.save(oldUser);
    }
}
