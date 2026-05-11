package com.example.demo.service;

import com.example.demo.model.GymMember;
import com.example.demo.model.GymMemberCreateRequest;
import com.example.demo.repository.GymMemberRepository;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

@Service
public class GymMemberService {
	private final GymMemberRepository repository;

	public GymMemberService(GymMemberRepository repository) {
		this.repository = repository;
	}

	public List<GymMember> getAllMembers() {
		return repository.findAll();
	}

	public GymMember getMember(long id) {
		return repository.findById(id)
			.orElseThrow(() -> new NoSuchElementException("Member not found: " + id));
	}

	public GymMember createMember(GymMemberCreateRequest request) {
		return repository.saveNew(request.name(), request.membershipType());
	}
}
