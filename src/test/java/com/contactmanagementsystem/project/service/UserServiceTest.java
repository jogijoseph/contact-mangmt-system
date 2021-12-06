package com.contactmanagementsystem.project.service;

import com.contactmanagementsystem.project.exception.InvalidPhoneNumberException;
import com.contactmanagementsystem.project.exception.UserNotFoundException;
import com.contactmanagementsystem.project.model.User;
import com.contactmanagementsystem.project.repository.UserRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    void testCreateUser_Success() {
        // Given
        String name = "john";
        String address = "kochi";
        String ph = "987654321";
        String email = "test@gmail";
        String countryCode = "GB";
        User expValue = User.builder().id(1).name(name).address(address).countryCode(countryCode).ph(ph).email(email).build();
        Mockito.when(userRepository.save(ArgumentMatchers.eq(expValue))).thenReturn(expValue);

        // When
        User user = userService.createUser(expValue);

        // Then
        assertEquals(user, expValue);
        //Mockito.verify(userRepository, Mockito.times(1)).save(ArgumentMatchers.eq(user));
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userArgumentCaptor.capture());
        User actual = userArgumentCaptor.getValue();
        assertEquals(actual, expValue);
    }

    @Test
    void testCreateUser_Failure_InvalidPh() {
        // Given
        String name = "john";
        String address = "kochi";
        String ph = "987654321";
        String email = "test@gmail";
        String countryCode = "GB";
        User user = User.builder().id(1).name(name).address(address).countryCode(countryCode).ph(ph).email(email).build();
        when(userRepository.existsByPh(anyString())).thenReturn(true);

        // Then
        Assertions.assertThrows(
                InvalidPhoneNumberException.class,
                () -> {
                    userService.createUser(user);
                });
        verify(userRepository, Mockito.never()).save(user);
    }

    @Test
    void testCreateUsers_Success() {
        // Given
        User userOne = User.builder().id(1).name("Mike").address("New York").countryCode("GB").ph("543218765").email("mike@test.com").build();
        User userTwo = User.builder().id(2).name("Adam").address("London").countryCode("GB").ph("76543218733").email("adam@test.com").build();
        User userThree = User.builder().id(3).name("Don").address("Paris").countryCode("GB").ph("98765434563").email("Don@test.com").build();
        List<User> expResult = new ArrayList<>();
        expResult.add(userOne);
        expResult.add(userTwo);
        expResult.add(userThree);
        when(userRepository.saveAll(ArgumentMatchers.eq(expResult))).thenReturn(expResult);

        // When
        userService.createUsers(expResult);

        // Then
        ArgumentCaptor<List<User>> listUserArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(userRepository, times(1)).saveAll(listUserArgumentCaptor.capture());
        List<User> actual = listUserArgumentCaptor.getValue();
        assertEquals(actual, expResult);
    }

    @Test
    void testCreateUsers_Failure_InvalidPh() {
        // Given
        User userOne = User.builder().id(1).name("Mike").address("New York").countryCode("GB").ph("543218765").email("mike@test.com").build();
        User userTwo = User.builder().id(2).name("Adam").address("London").countryCode("GB").ph("76543218733").email("adam@test.com").build();
        User userThree = User.builder().id(3).name("Don").address("Paris").countryCode("GB").ph("98765434563").email("Don@test.com").build();
        List<User> expResult = new ArrayList<>();
        expResult.add(userOne);
        expResult.add(userTwo);
        expResult.add(userThree);
        when(userRepository.existsByPh(anyString())).thenReturn(false).thenReturn(true);

        // When Then
        Assertions.assertThrows(
                InvalidPhoneNumberException.class,
                () -> {
                    userService.createUsers(expResult);
                });
        verify(userRepository, Mockito.never()).saveAll(expResult);
    }


    @Test
    void testGetUserById() {
        // Given
        User userOne = User.builder().id(1).name("Mike").address("New York").ph("543218765").email("mike@test.com").build();
        when(userRepository.findById(eq(userOne.getId()))).thenReturn(Optional.of(userOne));

        // When
        User user = userService.getUserById(userOne.getId());

        // Then
        Assertions.assertNotNull(user);
        assertEquals(user, userOne);
    }

    @Test
    void testGetUsers() {
        // Given
        User userOne = User.builder().id(1).name("Mike").address("New York").countryCode("GB").ph("543218765").email("mike@test.com").build();
        User userTwo = User.builder().id(2).name("Adam").address("London").countryCode("GB").ph("76543218733").email("adam@test.com").build();
        User userThree = User.builder().id(3).name("Don").address("Paris").countryCode("GB").ph("98765434563").email("Don@test.com").build();
        List<User> expResult = new ArrayList<>();
        expResult.add(userOne);
        expResult.add(userTwo);
        expResult.add(userThree);
        when(userRepository.findAll()).thenReturn(List.of(userOne,userTwo,userThree));
        
        // When
        List<User> user = userService.getUsers();
        // Then
        verify(userRepository, times(1)).findAll();
        assertEquals(user, expResult);
    }

    @Test
    void testDeleteUserById_Success() {
        // Given
        User expValue = User.builder().id(1).name("Mike").address("New York").ph("543218765").email("mike@test.com").build();
        when(userRepository.findById(eq(expValue.getId()))).thenReturn(Optional.of(expValue));

        // When
        userService.deleteUserById(expValue.getId());

        // Then
        verify(userRepository, times(1)).deleteById(expValue.getId());
    }

    @Test
    void testDeleteUserById_Failure_UserNotFound() {
        //Given
        User expResult = User.builder().id(1).name("Mike").address("New York").ph("543218765").email("mike@test.com").build();
        when(userRepository.findById(eq(expResult.getId()))).thenReturn(Optional.empty());

        // When Then
        Assertions.assertThrows(
                UserNotFoundException.class,
                () -> {
                    userService.deleteUserById(expResult.getId());
                });
        verify(userRepository, Mockito.never()).deleteById(expResult.getId());

    }

    @SneakyThrows
    @Test
    void testReadDataFromExcel_Success() {
        // Given
        User expResult = User.builder().id(0).name("Johnson").address("LA").countryCode("US").ph("+18965432345").email("bcd@gmail.com").build();
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("file.xls");
        MockMultipartFile file = new MockMultipartFile("filename", "file.xls", "application/vnd.ms-excel", inputStream);

        // When
        userService.readDataFromExcel(file);

        // Then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(3)).save(userArgumentCaptor.capture());
        User user = userArgumentCaptor.getValue();
        assertEquals(user, expResult);
    }

    @SneakyThrows
    @Test
    void testReadDataFromExcel_Failure_InvalidPh() {
        User user =
                User.builder()
                        .id(1)
                        .name("Mathew")
                        .address("Paris")
                        .countryCode("AD")
                        .ph("+376678902345")
                        .email("Test@gmail.com")
                        .build();
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("file.xls");
        MockMultipartFile file =
                new MockMultipartFile("filename", "file.xls", "application/vnd.ms-excel", inputStream);
        when(userRepository.existsByPh(user.getPh())).thenReturn(true);

        // When Then
        Assertions.assertThrows(
                InvalidPhoneNumberException.class,
                () -> {
                    userService.readDataFromExcel(file);
                });
        verify(userRepository, Mockito.never()).save(user);
    }
}