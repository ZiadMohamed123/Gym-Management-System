package com.example.demo.repository;

import com.example.demo.model.GymMember;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;

@Repository
public class GymMemberRepository {
	private final List<GymMember> members = new CopyOnWriteArrayList<>();
	private final AtomicLong idGenerator = new AtomicLong(1);

	// In-memory store for demo purposes.
	public List<GymMember> findAll() {
		return List.copyOf(members);
	}

	public Optional<GymMember> findById(long id) {
		return members.stream().filter(member -> member.id() == id).findFirst();
	}

	public GymMember saveNew(String name, String membershipType) {
		GymMember created = new GymMember(idGenerator.getAndIncrement(), name, membershipType);
		members.add(created);
		return created;
	}
}
