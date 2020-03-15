package com.revature.PollingApp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.revature.PollingApp.model.Role;
import com.revature.PollingApp.model.RoleName;

public interface RoleRepository extends JpaRepository<Role, Long> {
	
	Optional<Role> findByName(RoleName rolename);

}
