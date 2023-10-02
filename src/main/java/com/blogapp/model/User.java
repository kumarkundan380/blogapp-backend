package com.blogapp.model;

import com.blogapp.enums.Gender;
import com.blogapp.enums.UserStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

	@Id
	@GeneratedValue
	@Column(name = "user_id")
	private Integer userId;

	@Column(name = "user_name",unique = true,nullable = false)
	private String userName;
	
	@Column(name = "password")
	private String password;

	@Column(name = "first_name",nullable = false)
	private String firstName;
	
	@Column(name = "middle_name")
	private String middleName;
	
	@Column(name = "last_name")
	private String lastName;

	private String about;

	@Column(name = "user_image")
	private String userImage;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	@JsonManagedReference
	private Set<Address> addresses;

	@Column(name = "gender", nullable = false)
	@Enumerated(EnumType.STRING)
	private Gender gender;

	@Column(name = "phone_number")
	private String phoneNumber;
	
	@Column(name = "is_active")
	private Boolean isActive = Boolean.TRUE;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	@ToString.Exclude
	private Set<Post> posts;
	
	@OneToMany(mappedBy = "user")
	private Set<Comment> comments;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinTable(name = "user_role",
		joinColumns = {
				@JoinColumn(name = "user_id", referencedColumnName= "user_id")
		},
		inverseJoinColumns = { 
				@JoinColumn(name = "role_id", referencedColumnName= "role_id")
		}
	)
	private Set<Role> roles = new HashSet<>();
	
	@Column(name="is_user_verified")
	private Boolean isUserVerified = Boolean.FALSE;

	@Column(name = "status",nullable = false)
	@Enumerated(EnumType.STRING)
	private UserStatus status = UserStatus.ACTIVE;

	@Column(name = "account_non_expired", nullable = false)
	private Boolean accountNonExpired = Boolean.TRUE;

	@Column(name = "credentials_non_expired", nullable = false)
	private Boolean credentialsNonExpired = Boolean.TRUE;

	@Column(name = "account_non_locked", nullable = false)
	private Boolean accountNonLocked = Boolean.TRUE;

}
