package com.example.covid.service;

import com.example.covid.entity.Disease;
import com.example.covid.repository.DiseaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DiseaseService {
    @Autowired
    private DiseaseRepository repository;
    public List<Disease> findAll(){
        return repository.findAll();
    }
    public Disease findById(String id){
        Optional<Disease> diseaseOptional = repository.findById(id);
        return diseaseOptional.isPresent() ? diseaseOptional.get() : null;
    }
    public Disease add(Disease disease){
        return repository.save(disease);
    }
    public Disease update(Disease disease){
        return repository.save(disease);
    }
    public void delete(String id){
        Disease disease = findById(id);
        if(disease != null){
            repository.delete(disease);
        }
    }
}
