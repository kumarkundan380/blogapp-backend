package com.blogapp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private Integer addressId;
    @NotBlank(message = "AddressLine1 must not be null or empty")
    private String addressLine1;
    private String addressLine2;
    @NotBlank(message = "City must not be null or empty")
    private String city;
    @NotBlank(message = "State must not be null or empty")
    private String state;
    @NotBlank(message = "Country must not be null or empty")
    private String country;
    @NotBlank(message = "PostalCode must not be null or empty")
    private String postalCode;
}
