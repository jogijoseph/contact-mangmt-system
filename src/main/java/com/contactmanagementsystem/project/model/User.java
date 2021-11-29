package com.contactmanagementsystem.project.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "user_contacts")
public class User {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @Column(name = "user_name")
    @NotEmpty(message = "The full name is required.")
    @Size(min = 4, max = 100, message = "The length of full name must be between 4 and 100 characters.")
    private String name;

    @NotNull(message = "The address is required.")
    @Column(name = "address")
    private String address;

    @Column(name = "country_code")
    @NotNull(message = "The countryCode is required.")
    @Size(min = 2, max = 2, message = "The countryCode must be 2 characters.")
    private String countryCode;

    @Column(name = "phone_no", unique = true)
    //@Pattern(regexp = "^(\\+)[0-9]+", message = "Phone no. must not contain alphabets or special characters")
    private String ph;

    @NotEmpty(message = "The email address is required.")
    @Email(message = "The email address is invalid.", flags = { Pattern.Flag.CASE_INSENSITIVE })
    @Column(name = "email")
    private String email;
}
