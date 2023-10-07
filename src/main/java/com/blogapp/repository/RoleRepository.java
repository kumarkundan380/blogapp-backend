package com.blogapp.repository;

import com.blogapp.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface RoleRepository extends JpaRepository<Role, Integer> {

	Optional<Role> findByRoleName(String roleName);

	Set<Role> findByRoleNameIn(Set<String> userRoles);

}
