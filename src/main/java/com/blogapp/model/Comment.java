package com.blogapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "comments")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends BaseEntity {

	@Id
	@GeneratedValue
	@Column(name = "comment_id")
	private Integer commentId;

	@Column(columnDefinition="TEXT",nullable = false)
	private String content;
	
	@ManyToOne
	@JoinColumn(name = "post_id")
	@JsonBackReference
	private Post post;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
	@JsonManagedReference
	private Set<Activity> activities = new HashSet<>();

}
