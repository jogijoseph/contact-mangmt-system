package com.contactmanagementsystem.project.service;

import com.contactmanagementsystem.project.exception.UserNotFoundException;
import com.contactmanagementsystem.project.model.User;
import com.contactmanagementsystem.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /*
    Creates a user and insert into DB.
     */
    public User createUser(User user) {
        return userRepository.save(user);
    }

    /*
    Creates a list of users and insert into DB.
     */
    public List<User> createUsers(List<User> users) {
        return userRepository.saveAll(users);
    }

    /*
    Returns a user by id if present.
     */
    public User getUserById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    /*
    Returns all users.
     */
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    /*
    Delete a user by id if present. If not, throws user not found exception.
     */
    public void deleteUserById(int id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent())
            userRepository.deleteById(id);
        else
            throw new UserNotFoundException("id-" + id);
    }

}
