package com.example.covid.controller;

import com.example.covid.entity.CheckIn;
import com.example.covid.entity.User;
import com.example.covid.repository.UserRepository;
import com.example.covid.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @GetMapping
    public User getUserInfo() {
        return userService.getCurrentUserInfo();
    }

    @PutMapping
    public User updateUserInfo(@RequestBody Map<String, String> infoUpdated) {
        return userService.updateUserInfo(infoUpdated);
    }

    @GetMapping("/check-in-history")
    public List<CheckIn> getCheckInHistory() {
        return userService.getCheckInHistory();
    }

    @PostMapping("/check-in-history")
    public CheckIn insertCheckIn(@RequestBody CheckIn checkIn) {
        return userService.insertCheckIn(checkIn);
    }
    @PutMapping("/check-in-history")
    public CheckIn updateCheckIn(@RequestBody CheckIn checkIn) {
        return userService.updateCheckIn(checkIn);
    }
}
