package com.example.covid.service;

import com.example.covid.entity.Person;
import com.example.covid.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {
    @Autowired
    PersonRepository repository;

    public List<Person> findAll(){
        return repository.findAll();
    }
    public Person findById(String id){
        Optional<Person> personOptional = repository.findById(id);
        if (personOptional.isPresent()){
            return personOptional.get();
        }
        return null;
    }
    public Long count(){
        return repository.count();
    }
    public Person save(Person person){
        return repository.save(person);
    }
    public void delete(String id){
        Person person = findById(id);
        if(person != null){
            repository.delete(person);
        }
    }
}
