package com.blogapp.repository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.blogapp.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {
	Optional<User> findByUserName(String userName);
	@Modifying
	@Query("update User u set u.status='DELETED' where u.userId = ?1")
	void deleteUser(Integer userId);
	@Query("SELECT u FROM User u WHERE u.status = ?1 AND u.isUserVerified = ?2 ")
	Page<User> findAllUser(String status, Boolean isUserVerified, Pageable pageable);
	@Query("SELECT CASE WHEN u.status = 'DELETED' THEN true ELSE false END FROM User u WHERE u.userName = ?1 ")
	Boolean isDeletedUser(String userName);

	@Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.userName = ?1 ")
	Boolean isUserExist(String userName);

}
