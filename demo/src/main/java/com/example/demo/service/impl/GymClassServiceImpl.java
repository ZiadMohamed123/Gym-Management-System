package com.example.demo.service.impl;

import com.example.demo.dto.GymClassDto;
import com.example.demo.model.GymClass;
import com.example.demo.repository.GymClassRepository;
import com.example.demo.service.GymClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class GymClassServiceImpl implements GymClassService {

    private final GymClassRepository gymClassRepository;

    @Override
    public GymClass create(GymClassDto dto) {
        GymClass gymClass = GymClass.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .scheduleDateTime(dto.getScheduleDateTime())
                .maxCapacity(dto.getMaxCapacity())
                .cancelled(false)
                .createdBy(dto.getCreatedBy())
                .build();
        return gymClassRepository.save(gymClass);
    }

    @Override
    public GymClass update(GymClassDto dto) {
        GymClass gymClass = gymClassRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Class not found"));
        gymClass.setName(dto.getName());
        gymClass.setDescription(dto.getDescription());
        gymClass.setScheduleDateTime(dto.getScheduleDateTime());
        gymClass.setMaxCapacity(dto.getMaxCapacity());
        gymClass.setCreatedBy(dto.getCreatedBy());
        return gymClassRepository.save(gymClass);
    }

    @Override
    public void cancel(Long id) {
        GymClass gymClass = gymClassRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Class not found"));
        gymClass.setCancelled(true);
        gymClassRepository.save(gymClass);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GymClass> findById(Long id) {
        return gymClassRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GymClass> findAll() {
        return gymClassRepository.findByCancelledFalseOrderByScheduleDateTimeAsc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<GymClass> findUpcoming() {
        return gymClassRepository.findUpcomingClasses(LocalDateTime.now());
    }

    @Override
    @Transactional(readOnly = true)
    public long countTotal() {
        return gymClassRepository.count();
    }
}
