package com.example.covid.repository;

import com.example.covid.entity.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriberRepository extends JpaRepository<Subscriber, Integer> {
    List<Subscriber> findByCityId(Integer cityId);
}
