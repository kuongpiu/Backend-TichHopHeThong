package com.example.covid.repository;

import com.example.covid.entity.MessageInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageInfoRepository extends JpaRepository<MessageInfo, Integer> {
}
