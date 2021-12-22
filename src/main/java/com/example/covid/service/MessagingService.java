package com.example.covid.service;

import com.example.covid.entity.*;
import com.example.covid.repository.CityRepository;
import com.example.covid.repository.MessageInfoRepository;
import com.example.covid.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.*;

@Service
public class MessagingService {
    @Autowired
    private MessageInfoRepository messageInfoRepo;
    @Autowired
    private MessageRepository messageRepo;
    @Autowired
    private CityRepository cityRepo;
    @Autowired
    private SubscriberService subscriberService;

    public List<MessageInfo> getAllMessageInfo(){
        return messageInfoRepo.findAll();
    }
    private Message insertMessage(String content) {
        Message message = new Message();
        message.setContent(content);
        message.setCreated_user(getCurrentUsername());

        long millis = System.currentTimeMillis();
        Date date = new Date(millis);
        message.setDate(date);

        return this.messageRepo.save(message);
    }
    /*
    * function: insertMessageInfo(Map<String, String> inputMap)
    * inputMap = {
    *   cities: "1,2,3", -> id của các tỉnh, phân cách bằng dấu ','
    *   content: "Thông báo khẩn số 10...." -> nội dung cần gửi cho các tỉnh id trong cities
    * }
    * return các user có đăng ký nhận tin tức từ các tỉnh và nội dung của tin nhắn
    * */
    public Object insertMessageInfo(Map<String, String> inputMap) {
        String citiesIdRaw = inputMap.get("cities");
        String content = inputMap.get("content");

        Message message = this.insertMessage(content);
        List<City> cities = convertToCities(citiesIdRaw);

        this.saveMessageInfo(cities, message);
        Set<User> usersSubscribedCities = subscriberService.getUsersSubscribedCities(cities);

        Map<String, Object> response = new HashMap<>();
        response.put("users", usersSubscribedCities.toArray());
        response.put("content", content);
        return response;
    }
    private List<MessageInfo> saveMessageInfo(List<City> cities, Message message){
        List<MessageInfo> messageInfos = new ArrayList<>();
        for (City city : cities) {
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setMessage(message);
            messageInfo.setCity(city);
            messageInfos.add(messageInfoRepo.save(messageInfo));
        }
        return messageInfos;
    }
    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return username;
    }
    private List<City> convertToCities(String citiesIdRaw) {
        String[] citiesId = citiesIdRaw.split(",");
        List<City> cities = new ArrayList<>();

        for (String cityId : citiesId) {
            try {
                Optional<City> cityOptional = cityRepo.findById(Integer.parseInt(cityId));
                cityOptional.ifPresent(cities::add);
            } catch (Exception e) {
                System.out.println("không thể convert string cityId=[" + cityId + "] sang mã tỉnh - Integer" );
            }
        }
        return cities;
    }
}
