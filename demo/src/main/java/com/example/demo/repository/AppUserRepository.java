package com.example.demo.repository;

import com.example.demo.model.AppUser;
import com.example.demo.model.enums.UserRole;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
	Optional<AppUser> findByUsername(String username);
	boolean existsByUsername(String username);
	List<AppUser> findByRole(UserRole role);
}
