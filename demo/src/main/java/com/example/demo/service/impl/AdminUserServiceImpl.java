package com.example.demo.service.impl;

import com.example.demo.dto.TrainerCreateDto;
import com.example.demo.model.AppUser;
import com.example.demo.model.enums.UserRole;
import com.example.demo.repository.AppUserRepository;
import com.example.demo.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminUserServiceImpl implements AdminUserService {

	private final AppUserRepository appUserRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public void createTrainer(TrainerCreateDto dto) {
		if (appUserRepository.existsByUsername(dto.getUsername())) {
			throw new IllegalArgumentException("Username is already taken.");
		}

		AppUser trainer = AppUser.builder()
				.username(dto.getUsername())
				.password(passwordEncoder.encode(dto.getPassword()))
				.role(UserRole.TRAINER)
				.enabled(true)
				.build();

		appUserRepository.save(trainer);
	}
}
