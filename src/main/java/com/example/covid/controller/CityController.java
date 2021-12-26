package com.example.covid.controller;

import com.example.covid.entity.City;
import com.example.covid.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/city")
public class CityController {
    @Autowired
    private CityRepository cityRepository;
    @GetMapping("/all")
    public List<City> findAll(){
        System.out.println("find all cities");
        return cityRepository.findAll();
    }
}
