package com.contactmanagementsystem.project.repository;

import com.contactmanagementsystem.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByPh(String ph);
}
