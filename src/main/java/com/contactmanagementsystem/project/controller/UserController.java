package com.contactmanagementsystem.project.controller;

import com.contactmanagementsystem.project.dto.Response;
import com.contactmanagementsystem.project.exception.UserNotFoundException;
import com.contactmanagementsystem.project.model.User;
import com.contactmanagementsystem.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/cms")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/addUser")
    public ResponseEntity<Response> addUser(@Valid @RequestBody User user) {
        userService.createUser(user);
        return new ResponseEntity<>(Response.builder()
                .message("User created")
                .success(true)
                .build(), HttpStatus.CREATED);
    }

    @PostMapping("/addUsers")
    public ResponseEntity<Response> addUsers(@Valid @RequestBody List<User> users) {
        userService.createUsers(users);
        return new ResponseEntity<>(Response.builder()
                .message("Users created")
                .success(true)
                .build(), HttpStatus.CREATED);
    }

    @GetMapping("/user/{id}")
    public User getUserById(@PathVariable int id) {
        User user = userService.getUserById(id);
        if (user == null)
            throw new UserNotFoundException("id-" + id);
        return user;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getUsers();
    }

    @DeleteMapping("/user/delete/{id}")
    public ResponseEntity<Response> deleteUser(@PathVariable int id) {
        try {
            userService.deleteUserById(id);
            return new ResponseEntity<>(Response.builder()
                    .message("Deleted")
                    .success(true)
                    .build(), HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(
                    Response.builder()
                            .message("Bad Request. Invalid user.")
                            .success(false)
                            .build(), HttpStatus.BAD_REQUEST);
        }
    }
}
