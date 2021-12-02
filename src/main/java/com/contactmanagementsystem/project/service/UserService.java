package com.contactmanagementsystem.project.service;

import com.contactmanagementsystem.project.exception.PhoneNoAlreadyPresentException;
import com.contactmanagementsystem.project.exception.UserNotFoundException;
import com.contactmanagementsystem.project.model.User;
import com.contactmanagementsystem.project.repository.UserRepository;
import com.contactmanagementsystem.project.util.CountryToPhonePrefixUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

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
        user.setPh(CountryToPhonePrefixUtil.prefixCode(user.getCountryCode()).concat(user.getPh()));
        if (userRepository.existsByPh(user.getPh()))
            throw new PhoneNoAlreadyPresentException("Phone Number already present");
        else
            return userRepository.save(user);
    }

    /*
    Creates a list of users and insert into DB.
     */
    public List<User> createUsers(List<User> users) {
        users.stream().forEach(user ->
        {
            user.setPh(CountryToPhonePrefixUtil.prefixCode(user.getCountryCode()).concat(user.getPh()));
            if (userRepository.existsByPh(user.getPh()))
                throw new PhoneNoAlreadyPresentException("Phone Number already present");
        });
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

    /**
     * @param file
     * @throws Exception
     */
    public void readDataFromExcel(MultipartFile file) throws Exception {
        Workbook workbook = new HSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        IntStream.range(0, sheet.getPhysicalNumberOfRows())
                .skip(1)
                .forEach(index -> {
                    User user = new User();
                    user.setName(sheet.getRow(index).getCell(0).toString());
                    user.setAddress(sheet.getRow(index).getCell(1).toString());
                    user.setCountryCode(sheet.getRow(index).getCell(2).toString());
                    user.setPh(CountryToPhonePrefixUtil.prefixCode(user.getCountryCode())
                            .concat(sheet.getRow(index).getCell(3).toString()));
                    user.setEmail(sheet.getRow(index).getCell(4).toString());
                    if (userRepository.existsByPh(user.getPh())) {
                        throw new PhoneNoAlreadyPresentException("Phone Number already present");
                    } else {
                        userRepository.save(user);
                    }
                });

    }

}


