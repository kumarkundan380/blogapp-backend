package com.blogapp.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.blogapp.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import com.blogapp.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

	Optional<Role> findByRoleName(UserRole userRole);

	Set<Role> findByRoleNameIn(List<UserRole> userRoles);

}
