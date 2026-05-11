package com.example.demo.controller;

import com.example.demo.model.GymMember;
import com.example.demo.model.GymMemberCreateRequest;
import com.example.demo.service.GymMemberService;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/members")
public class GymMemberController {
	private final GymMemberService service;

	public GymMemberController(GymMemberService service) {
		this.service = service;
	}

	@GetMapping
	public List<GymMember> getMembers() {
		return service.getAllMembers();
	}

	@GetMapping("/{id}")
	public GymMember getMember(@PathVariable long id) {
		try {
			return service.getMember(id);
		} catch (NoSuchElementException ex) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
		}
	}

	@PostMapping
	public ResponseEntity<GymMember> createMember(@RequestBody GymMemberCreateRequest request) {
		GymMember created = service.createMember(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}
}
