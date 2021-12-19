package com.example.covid.service;

import com.example.covid.entity.Patient;
import com.example.covid.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService {
    @Autowired
    PatientRepository patientRepository;

    public List<Patient> findAll(){
        List<Patient> patients = patientRepository.findAll();
        System.out.println("Find all patients");
        return patients;
    }
    public Patient findById(Integer id){
        Optional<Patient> patientOptional = patientRepository.findById(id);
        System.out.println("find by id, id= " + id);
        return patientOptional.isPresent() ? patientOptional.get() : null;
    }
    public Patient insert(Patient patient){
        System.out.println("insert new patient, id= " + patient.getId());
        return patientRepository.save(patient);
    }
    public Patient update(Patient patient){
        System.out.println("update patient, id= " + patient.getId());
        return patientRepository.save(patient);
    }
    public void delete(Integer id){
        Patient patient = findById(id);
        if(patient != null) {
            System.out.println("delete patient, id= " + id);
            patientRepository.delete(patient);
        }else{
            System.out.println("patient not exist in database, id= " + id);
        }
    }
}
