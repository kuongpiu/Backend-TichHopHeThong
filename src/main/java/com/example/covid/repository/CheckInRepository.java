package com.example.covid.repository;

import com.example.covid.entity.CheckIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckInRepository extends JpaRepository<CheckIn, Integer> {
    List<CheckIn> findByUsername(String username);
    List<CheckIn> findByUsernameOrderByDateDesc(String username);
}
