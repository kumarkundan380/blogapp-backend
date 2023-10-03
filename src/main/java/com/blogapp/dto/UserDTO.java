package com.blogapp.dto;

import com.blogapp.enums.Gender;
import com.blogapp.enums.UserStatus;
import com.blogapp.serializer.UserDTOSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties( value = {"password"}, allowSetters =  true )
@JsonSerialize(using = UserDTOSerializer.class)
public class UserDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer userId;

    @NotBlank(message = "Username must not be null or empty")
    @Size(min = 4, message = "Username must be Minimum of 4 characters")
    @Email(message = "Username must be valid")
    private String userName;

    private String password;

    @NotBlank(message = "First Name must not be null or empty")
    private String firstName;

    private String middleName;

    private String lastName;

    @NotBlank(message = "About must not be null or empty")
    private String about;

    private String userImage;

    private Gender gender;

    private String phoneNumber;
    private UserStatus userStatus;
    private Boolean isUserVerified;
    private Set<RoleDTO> roles = new HashSet<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isAccountExpired;
    private Boolean isCredentialsExpired;
    private Boolean isAccountLocked;

}
