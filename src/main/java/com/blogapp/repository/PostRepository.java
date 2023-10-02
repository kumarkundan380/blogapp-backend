package com.blogapp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.blogapp.model.Category;
import com.blogapp.model.Post;
import com.blogapp.model.User;


public interface PostRepository extends JpaRepository<Post, Integer> {
	
	Page<Post> findByUser(User user, Pageable pageable);
	Page<Post> findByCategory(Category category, Pageable pageable);
	Page<Post> findByPostTitleContaining(String postTitle, Pageable pageable);

}
