package com.example.covid.controller;

import com.example.covid.entity.*;
import com.example.covid.repository.*;
import com.example.covid.service.MessagingService;
import com.example.covid.service.SubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/message")
public class MessagingController {
    @Autowired
    private SubscriberService subscriberService;
    @Autowired
    private MessagingService messagingService;

    @GetMapping("/all")
    public List<MessageInfo> getAllMessageInfo() {
        return messagingService.getAllMessageInfo();
    }

    @PostMapping
    public Object insertMessageInfo(@RequestBody Map<String, String> inputMap) {
        return messagingService.insertMessageInfo(inputMap);
    }

    @GetMapping("/subscribe")
    public List<City> getSubscribedCities() {
        return subscriberService.getSubscribedCities();
    }

    @PostMapping("/subscribe")
    public List<City> subscribe(@RequestBody Map<String, String> input) {
        return subscriberService.subscribe(input);
    }
}