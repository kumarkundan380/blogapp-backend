package com.blogapp.repository;

import com.blogapp.enums.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.blogapp.model.Category;
import com.blogapp.model.Post;
import com.blogapp.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface PostRepository extends JpaRepository<Post, Integer> {
	
	Page<Post> findByUser(User user, Pageable pageable);
	Page<Post> findByCategory(Category category, Pageable pageable);
	Page<Post> findByPostTitleContaining(String postTitle, Pageable pageable);
	@Modifying
	@Query("update Post p set p.postStatus='DELETED' where p.postId = ?1")
	void deletePost(Integer postId);

	@Query("SELECT p FROM Post p WHERE p.postStatus in ?1")
	Page<Post> findAllPostByStatus(List<PostStatus> postStatus, Pageable pageable);

	@Query("SELECT p Post FROM Post p WHERE p.postId = ?1 and p.postStatus='APPROVED'")
	Optional<Post> getPostById(Integer postId);

}
