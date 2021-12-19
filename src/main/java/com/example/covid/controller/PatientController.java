package com.example.covid.controller;

import com.example.covid.entity.Patient;
import com.example.covid.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("/covid")
public class PatientController {
    @Autowired
    PatientService patientService;

    @GetMapping("/patients")
    public List<Patient> findAll() {
        return patientService.findAll();
    }

    @GetMapping("/patient")
    public Patient findById(@RequestParam Integer id) {
        return patientService.findById(id);
    }

    @PostMapping("/patient")
    public Patient insert(@RequestBody Patient patient) {
        return patientService.insert(patient);
    }

    @PutMapping("/patient")
    public Patient update(@RequestBody Patient patient) {
        return patientService.update(patient);
    }

    @DeleteMapping("/patient")
    public void delete(@RequestParam Integer id){
        patientService.delete(id);
    }
}
