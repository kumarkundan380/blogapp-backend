package com.blogapp.repository;

import com.blogapp.enums.UserRole;
import com.blogapp.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface RoleRepository extends JpaRepository<Role, Integer> {

	Optional<Role> findByRoleName(UserRole userRole);

	Set<Role> findByRoleNameIn(Set<UserRole> userRoles);

}
