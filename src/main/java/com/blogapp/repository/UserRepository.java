package com.blogapp.repository;
import java.util.List;
import java.util.Optional;

import com.blogapp.enums.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.blogapp.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {
	Optional<User> findByUserName(String userName);
	@Modifying
	@Query("UPDATE User u set u.status='DELETED' WHERE u.userId = ?1")
	void deleteUser(Integer userId);
	@Query("SELECT u FROM User u WHERE u.status = ?1 AND u.isUserVerified = ?2 ")
	Page<User> findAllUser(String status, Boolean isUserVerified, Pageable pageable);
	@Query("SELECT CASE WHEN u.status = 'DELETED' THEN true ELSE false END FROM User u WHERE u.userName = ?1 ")
	Boolean isDeletedUser(String userName);

	@Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.userName = ?1 ")
	Boolean isUserExist(String userName);

	User getUserByUserName(String userName);

	@Query("SELECT u.status FROM User u")
	List<String> getAllUserStatus();

	@Query("SELECT count(*) FROM User u WHERE u.isUserVerified = ?1")
	Long countAllPendingUser(Boolean isUserVerified);

	@Query("SELECT count(*) FROM User u")
	Long countAllUser();

}
