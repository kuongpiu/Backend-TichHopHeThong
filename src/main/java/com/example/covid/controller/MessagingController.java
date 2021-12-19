package com.example.covid.controller;

import com.example.covid.entity.*;
import com.example.covid.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.Instant;
import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/message")
public class MessagingController {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private MessageInfoRepository messageInfoRepository;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private SubscriberRepository subscriberRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/all")
    public List<MessageInfo> findAll() {
        return messageInfoRepository.findAll();
    }

    @PostMapping
    public Object insert(@RequestBody Map<String, String> inputMap) {
        String citiesIdRaw = inputMap.get("cities");
        String content = inputMap.get("content");

        Message message = this.insertMessage(content);

        Set<User> telegramUserSubs = new HashSet<>();

        List<City> cities = convertToCityObj(citiesIdRaw);
//        List<MessageInfo> messageInfos = new ArrayList<>();

        for (City city : cities) {
            List<Subscriber> subscribersByCity = subscriberRepository.findByCityId(city.getId());
            for (Subscriber subscriber : subscribersByCity) {
                String username = subscriber.getUsername();
                User user = userRepository.findByUsername(username);
                telegramUserSubs.add(user);
            }
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setMessage(message);
            messageInfo.setCity(city);
            messageInfoRepository.save(messageInfo);
//            messageInfos.add(messageInfoRepository.save(messageInfo));
        }
        Map<String, Object> sendingMessageVieTelegram = new HashMap<>();
        sendingMessageVieTelegram.put("users", telegramUserSubs.toArray());
        sendingMessageVieTelegram.put("content", content);

        return sendingMessageVieTelegram;
//        return messageInfos; // id, City, Message
    }

    private List<City> convertToCityObj(String citiesIdRaw) {
        String[] citiesId = citiesIdRaw.split(",");
        List<City> cities = new ArrayList<>();

        for (String cityId : citiesId) {
            Optional<City> cityOptional = cityRepository.findById(Integer.parseInt(cityId));
            if (cityOptional.isPresent()) {
                cities.add(cityOptional.get());
            }
        }
        return cities;
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return username;
    }

    private Message insertMessage(String content) {
        Message message = new Message();
        message.setContent(content);
        message.setCreated_user(getCurrentUsername());
        long millis = System.currentTimeMillis();
        Date date = new Date(millis);
        message.setDate(date);
        return this.messageRepository.save(message);
    }

    @GetMapping("/subscribe")
    public List<City> getSubscribeCities() {
        String username = getCurrentUsername();
        List<Subscriber> subscribers = subscriberRepository.findByUsername(username);
        List<City> cities = new ArrayList<>();
        for(Subscriber subscriber: subscribers){
            Optional<City> cityOptional = cityRepository.findById(subscriber.getCityId());
            if(cityOptional.isPresent()){
                cities.add(cityOptional.get());
            }
        }
        return cities;
    }

    @PostMapping("/subscribe")
    public List<City> subscribe(@RequestBody Map<String, String> input) {
        String username = getCurrentUsername();
        String citiesIdRaw = input.get("cities");
        String[] newCitiesId = citiesIdRaw.split(",");

        Set<Integer> newCitiesIdSet = new HashSet<>();
        for (String newCityId: newCitiesId){
            try{
                newCitiesIdSet.add(Integer.parseInt(newCityId));
            }catch (NumberFormatException e){
                e.printStackTrace();
            }
        }
        List<Subscriber> oldSubscribers = subscriberRepository.findByUsername(username);
        List<Integer> oldCitiesId = new ArrayList<>();
        for(Subscriber subscriber: oldSubscribers){
            oldCitiesId.add(subscriber.getCityId());
        }
        for (Integer oldCityId: oldCitiesId){
            if (newCitiesIdSet.contains(oldCityId)){
                newCitiesIdSet.remove(oldCityId);
                continue;
            }
            Subscriber subscriber = subscriberRepository.findByCityIdAndUsername(oldCityId, username);
            if(subscriber != null){
                subscriberRepository.delete(subscriber);
            }
        }

        if(!newCitiesIdSet.isEmpty()){
            for (Object cityId: newCitiesIdSet.toArray()) {
                try{
                    Subscriber Subscriber = new Subscriber();
                    Subscriber.setCityId((Integer) cityId);
                    Subscriber.setUsername(username);
                    subscriberRepository.save(Subscriber);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        return this.getSubscribeCities();
    }
}
