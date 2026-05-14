package com.example.demo.service;

import com.example.demo.dto.GymClassDto;
import com.example.demo.model.GymClass;

import java.util.List;
import java.util.Optional;

public interface GymClassService {
    GymClass create(GymClassDto dto);
    GymClass update(GymClassDto dto);
    void cancel(Long id);
    Optional<GymClass> findById(Long id);
    List<GymClass> findAll();
    List<GymClass> findUpcoming();
    List<GymClass> findByCreator(String createdBy);
    long countTotal();
}
