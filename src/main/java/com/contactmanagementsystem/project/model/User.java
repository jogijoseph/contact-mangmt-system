package com.contactmanagementsystem.project.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Size;

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
    @Size(min = 4)
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_no", unique = true)
    private String ph;

    @Column(name = "email")
    private String email;


}
