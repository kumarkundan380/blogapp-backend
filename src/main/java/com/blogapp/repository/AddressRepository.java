package com.blogapp.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blogapp.model.Address;
import com.blogapp.model.User;

public interface AddressRepository extends JpaRepository<Address, Integer> {
	Set<Address> findByUser(User user);

}
