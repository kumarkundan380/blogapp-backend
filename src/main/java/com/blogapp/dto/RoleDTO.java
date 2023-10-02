package com.blogapp.dto;

import com.blogapp.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private UserRole roleName;
    private String description;

}
