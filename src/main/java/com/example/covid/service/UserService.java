package com.example.covid.service;

import com.example.covid.entity.CheckIn;
import com.example.covid.entity.User;
import com.example.covid.repository.CheckInRepository;
import com.example.covid.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private CheckInRepository checkInRepo;

    private enum INFO_CAN_UPDATE {
        address,
        dob,
        gender,
        name,
        phoneNumber,
        telegramUsername,
        telegramUid
    }

    public User getCurrentUserInfo() {
        return userRepo.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    public User updateUserInfo(Map<String, String> infoUpdated) {
        User currentUser = getCurrentUserInfo();
        currentUser.setAddress(infoUpdated.get(INFO_CAN_UPDATE.address.toString()));
        try {
            currentUser.setDob(Date.valueOf(infoUpdated.get(INFO_CAN_UPDATE.dob.toString())));
        } catch (IllegalArgumentException e) {
            System.out.println("Ngay sinh khong hop le, vui long kiem tra lai");
        }
        currentUser.setGender(infoUpdated.get(INFO_CAN_UPDATE.gender.toString()));
        currentUser.setName(infoUpdated.get(INFO_CAN_UPDATE.name.toString()));
        currentUser.setPhoneNumber(infoUpdated.get(INFO_CAN_UPDATE.phoneNumber.toString()));
        currentUser.setTelegramUsername(infoUpdated.get(INFO_CAN_UPDATE.telegramUsername.toString()));
        currentUser.setTelegramUid(infoUpdated.get(INFO_CAN_UPDATE.telegramUid.toString()));
        return userRepo.save(currentUser);
    }

    public List<CheckIn> getAllCheckInHistory() {
        User currentUser = getCurrentUserInfo();
        if (currentUser != null) {
            String username = currentUser.getUsername();
            return checkInRepo.findByUsernameOrderByDateDesc(username);
        }
        return null;
    }

    public CheckIn getCheckInHistoryById(Integer id){
        Optional<CheckIn> optionalCheckIn = checkInRepo.findById(id);
        if(optionalCheckIn.isPresent()){
            CheckIn checkIn = optionalCheckIn.get();
            if(checkIn.getUsername().equals(SecurityContextHolder.getContext().getAuthentication().getName())){
                return checkIn;
            }else{
                return null;
            }
        }else{
            return null;
        }
    }

    public CheckIn insertCheckIn(CheckIn checkIn) {
        User currentUser = getCurrentUserInfo();
        if (currentUser != null) {
            checkIn.setUsername(currentUser.getUsername());
            return checkInRepo.save(checkIn);
        }
        return null;
    }

    public CheckIn updateCheckIn(CheckIn checkIn) {
        User currentUser = getCurrentUserInfo();
        if(currentUser != null){
            String username = currentUser.getUsername();
            Integer checkInId = checkIn.getId();
            Optional<CheckIn> checkInOptional = checkInRepo.findById(checkInId);
            if(checkInOptional.isPresent() && checkInOptional.get().getUsername().equals(username)){
                checkIn.setUsername(username);
                return checkInRepo.save(checkIn);
            }
            return null;
        }
        return null;
    }
}
