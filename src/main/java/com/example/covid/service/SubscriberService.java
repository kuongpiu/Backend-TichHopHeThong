package com.example.covid.service;

import com.example.covid.entity.City;
import com.example.covid.entity.MessageInfo;
import com.example.covid.entity.Subscriber;
import com.example.covid.entity.User;
import com.example.covid.repository.CityRepository;
import com.example.covid.repository.SubscriberRepository;
import com.example.covid.repository.UserRepository;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SubscriberService {
    @Autowired
    private SubscriberRepository subscriberRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private CityRepository cityRepo;
    public Set<User> getUsersSubscribedCities(List<City> cities){
        Set<User> users = new HashSet<>();
        for (City city : cities) {
            List<Subscriber> subscribersByCity = subscriberRepo.findByCityId(city.getId());
            for (Subscriber subscriber : subscribersByCity) {
                String username = subscriber.getUsername();
                User user = userRepo.findByUsername(username);
                users.add(user);
            }
        }
        return users;
    }
    public List<City> getSubscribedCities(){
        return getSubscribedCitiesByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
    }
    private List<City> getSubscribedCitiesByUsername(String username) {
        List<Subscriber> subscribers = subscriberRepo.findByUsername(username);
        List<City> cities = new ArrayList<>();
        for (Subscriber subscriber : subscribers) {
            Optional<City> cityOptional = cityRepo.findById(subscriber.getCityId());
            cityOptional.ifPresent(cities::add);
        }
        return cities;
    }
    /*
    * function subscribe(Map<String, String> input)
    * Chức năng: đăng ký nhận tin cho user, đầu vào là chuỗi các mã tỉnh đăng ký
    *
    * do cần phải xóa các thành phố mà đã đăng ký lần trước nhưng lần này không có (unsubscribe)
    * */
    public List<City> subscribe(Map<String, String> input) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String citiesIdRaw = input.get("cities");
        String[] newCitiesId = citiesIdRaw.split(",");

        Set<Integer> newCitiesIdSet = new HashSet<>();
        for (String newCityId : newCitiesId) {
            try {
                newCitiesIdSet.add(Integer.parseInt(newCityId));
            } catch (NumberFormatException e) {
                System.out.println("[" + newCityId + "] Không thể convert sang mã tỉnh !");
            }
        }

        List<City> oldSubscribedCities = getSubscribedCities();
        for (City oldCity : oldSubscribedCities) {
            Integer oldCityId = oldCity.getId();
            // nếu tỉnh nào đã đăng ký từ trước thì loại bỏ khỏi newCitiesIdSet
            if (newCitiesIdSet.contains(oldCityId)) {
                newCitiesIdSet.remove(oldCityId);
                continue;
            }
            // xóa các tỉnh mà không có trong danh sách đăng ký
            Subscriber subscriber = subscriberRepo.findByCityIdAndUsername(oldCityId, username);
            if (subscriber != null) {
                subscriberRepo.delete(subscriber);
            }
        }
        /*
            rồi. bây giờ newCitiesIdSet chỉ chứa các tỉnh chưa được đăng ký
            bắt đầu đăng ký ( lưu )
        */
        for (Object cityId : newCitiesIdSet.toArray()) {
            Subscriber Subscriber = new Subscriber();
            Subscriber.setCityId((Integer) cityId);
            Subscriber.setUsername(username);
            subscriberRepo.save(Subscriber);
        }
        return getSubscribedCities();
    }

}
