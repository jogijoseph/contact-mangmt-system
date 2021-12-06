package com.contactmanagementsystem.project.service;

import com.contactmanagementsystem.project.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    public User createUser(User user);

    public List<User> createUsers(List<User> users);

    public User getUserById(int id);

    public List<User> getUsers();

    public void deleteUserById(int id);

    public void readDataFromExcel(MultipartFile file) throws Exception;

}
