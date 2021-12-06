package com.contactmanagementsystem.project.controller;

import com.contactmanagementsystem.project.dto.Response;
import com.contactmanagementsystem.project.exception.UserNotFoundException;
import com.contactmanagementsystem.project.model.User;
import com.contactmanagementsystem.project.service.UserServiceImpl;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@Log4j2
@RequestMapping(path = "/cms")
public class UserController {
    @Autowired
    private UserServiceImpl userServiceImpl;


    @PostMapping("/addUser")
    public ResponseEntity<Response> addUser(@Valid @RequestBody User user) {
        log.info("Creating user");
        userServiceImpl.createUser(user);
        return new ResponseEntity<>(Response.builder()
                .message("User created")
                .success(true)
                .build(), HttpStatus.CREATED);
    }

    @PostMapping("/addUsers")
    public ResponseEntity<Response> addUsers(@Valid @RequestBody List<User> users) {
        userServiceImpl.createUsers(users);
        return new ResponseEntity<>(Response.builder()
                .message("Users created")
                .success(true)
                .build(), HttpStatus.CREATED);
    }

    @GetMapping("/user/{id}")
    public User getUserById(@PathVariable int id) {
        return userServiceImpl.getUserById(id);
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userServiceImpl.getUsers();
    }

    @DeleteMapping("/user/delete/{id}")
    public ResponseEntity<Response> deleteUser(@PathVariable int id) {
        try {
            userServiceImpl.deleteUserById(id);
            return new ResponseEntity<>(Response.builder()
                    .message("Deleted")
                    .success(true)
                    .build(), HttpStatus.OK);
        } catch (UserNotFoundException e) {
            log.error("User not found");
            return new ResponseEntity<>(
                    Response.builder()
                            .message("Bad Request. User not found.")
                            .success(false)
                            .build(), HttpStatus.BAD_REQUEST);
        }
    }

    @SneakyThrows
    @PostMapping("/upload")
    public ResponseEntity<Response> upload(@RequestParam("file") MultipartFile file) {
        userServiceImpl.readDataFromExcel(file);
        return new ResponseEntity<>(Response.builder()
                .message("Users created")
                .success(true)
                .build(), HttpStatus.CREATED);
    }

}
