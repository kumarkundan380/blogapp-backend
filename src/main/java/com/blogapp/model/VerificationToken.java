package com.blogapp.model;

import com.blogapp.enums.TokenType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "verificationToken")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VerificationToken extends BaseEntity {

	@Id
	@GeneratedValue
	@Column(name = "token_id")
	private Integer tokenId;
	
	@Column(name="token", updatable = false)
    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false,unique = false, name = "user_id")
    private User user;

	@Column(name = "token_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private TokenType tokenType;

}
