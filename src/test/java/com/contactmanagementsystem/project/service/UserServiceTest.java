package com.contactmanagementsystem.project.service;

import com.contactmanagementsystem.project.exception.PhoneNoAlreadyPresentException;
import com.contactmanagementsystem.project.exception.UserNotFoundException;
import com.contactmanagementsystem.project.model.User;
import com.contactmanagementsystem.project.repository.UserRepository;
import com.contactmanagementsystem.project.util.CountryToPhonePrefixUtil;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void init() {
        userService = new UserService(userRepository);
    }

    @Test
    void testCreateUser() {
        // Given
        String name = "john";
        String address = "kochi";
        String ph = "987654321";
        String email = "test@gmail";
        String countryCode ="GB";
        User user = User.builder().id(1).name(name).address(address).countryCode(countryCode).ph(ph).email(email).build();
        //Mockito.when(userRepository.save(ArgumentMatchers.eq(user))).thenReturn(user);

        //when
        userService.createUser(user);
        //Assertions.assertEquals(user1, user);
        //Mockito.verify(userRepository, Mockito.times(1)).save(ArgumentMatchers.eq(user));

        //then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userArgumentCaptor.capture());
        User value = userArgumentCaptor.getValue();
        ph= CountryToPhonePrefixUtil.prefixCode(countryCode).concat(ph);
        Assertions.assertEquals(value, user);
    }

    @Test
    void testCreateUsers() {
        //given
        User userOne = User.builder().id(1).name("Mike").address("New York").ph("543218765").email("mike@test.com").build();
        User userTwo = User.builder().id(2).name("Adam").address("London").ph("76543218733").email("adam@test.com").build();
        User userThree = User.builder().id(3).name("Don").address("Paris").ph("98765434563").email("Don@test.com").build();
        List<User> expResult = new ArrayList<>();
        expResult.add(userOne);
        expResult.add(userTwo);
        expResult.add(userThree);

        //when
        userService.createUsers(expResult);

        //then
        ArgumentCaptor<List<User>> listUserArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(userRepository, times(1)).saveAll(listUserArgumentCaptor.capture());
        List<User> value = listUserArgumentCaptor.getValue();
        Assertions.assertEquals(value, expResult);


    }

    @Test
    void testGetUserById() {
        //given
        User userOne = User.builder().id(1).name("Mike").address("New York").ph("543218765").email("mike@test.com").build();
        when(userRepository.findById(eq(userOne.getId()))).thenReturn(Optional.of(userOne));

        //when
        User user = userService.getUserById(userOne.getId());

        //then
        Assertions.assertNotNull(user);
        Assertions.assertEquals(user, userOne);
    }

    @Test
    void testGetUsers() {
        //when
        userService.getUsers();
        //then
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testDeleteUserById() {
        User userOne = User.builder().id(1).name("Mike").address("New York").ph("543218765").email("mike@test.com").build();
        when(userRepository.findById(eq(userOne.getId()))).thenReturn(Optional.of(userOne));
        // when
        userService.deleteUserById(userOne.getId());
        // then
        verify(userRepository, times(1)).deleteById(userOne.getId());
    }

    @Test
    void testDeleteUserById_whenNull() {

        User userOne = User.builder().id(1).name("Mike").address("New York").ph("543218765").email("mike@test.com").build();
        try {
            when(userRepository.findById(eq(userOne.getId()))).thenReturn(Optional.empty());
            // when
            userService.deleteUserById(userOne.getId());
            // then}
        } catch (UserNotFoundException e) {
            verify(userRepository, Mockito.never()).deleteById(userOne.getId());
        }
    }
    @SneakyThrows
    @Test
    void testReadDataFromExcel() {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("file.xls");
        MockMultipartFile file = new MockMultipartFile("filename", "file.xls", "application/vnd.ms-excel", inputStream);
        userService.readDataFromExcel(file);
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(3)).save(userArgumentCaptor.capture());
        User value = userArgumentCaptor.getValue();
    }

    @SneakyThrows
    @Test
    void testReadDataFromExcel_whenPhoneNumberPresent() {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("file.xls");
        MockMultipartFile file = new MockMultipartFile("filename", "file.xls", "application/vnd.ms-excel", inputStream);
        User user =User.builder().id(1).name("Mathew").address("Paris").countryCode("AD").ph("+376678902345").email("Test@gmail.com").build();
        try {
            when(userRepository.existsByPh(user.getPh())).thenReturn(true);
            userService.readDataFromExcel(file);
        }catch (PhoneNoAlreadyPresentException e) {
                verify(userRepository, Mockito.never()).save(user);
            }
    }
}