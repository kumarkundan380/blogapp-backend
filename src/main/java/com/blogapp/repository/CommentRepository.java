package com.blogapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blogapp.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

}
