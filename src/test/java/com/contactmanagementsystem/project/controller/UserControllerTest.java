package com.contactmanagementsystem.project.controller;

import com.contactmanagementsystem.project.model.User;
import com.contactmanagementsystem.project.repository.UserRepository;
import com.contactmanagementsystem.project.util.CountryToPhonePrefixUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
class UserControllerTest {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private UserRepository userRepository;


    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();

    }

    @BeforeEach
    public void setUp() {
        this.mockMvc =
                webAppContextSetup(this.wac).build();
    }

    @Test
    void testAddUser_Success() throws Exception {
        // Given
        User user = User.builder().id(1).name("Mike").address("New York").countryCode("IN").ph("88888990543").email("mike@test.com").build();

        // When
        mockMvc.perform(
                        post("/cms/addUser")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(user)))
                // Then
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)));

        Optional<User> actual = userRepository.findById(user.getId());
        user.setPh(CountryToPhonePrefixUtil.prefixCode(user.getCountryCode()).concat(user.getPh()));
        Assertions.assertEquals(user, actual.get());
    }

    @Test
    void testAddUsers_Success() throws Exception {
        // Given
        User userOne = User.builder().id(1).name("Mike").countryCode("IN").address("New York").ph("543218765").email("mike@test.com").build();
        User userTwo = User.builder().id(2).name("John").countryCode("IN").address("New York").ph("9876543").email("mike@test.com").build();
        User userThree = User.builder().id(3).name("Adam").countryCode("IN").address("New York").ph("654345678").email("mike@test.com").build();
        List<User> expValue = new ArrayList<>();
        expValue.add(userOne);
        expValue.add(userTwo);
        expValue.add(userThree);

        // When
        mockMvc.perform(
                        post("/cms/addUsers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(expValue)))
                // Then
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)));
        List<User> users = userRepository.findAll();
        expValue.stream().forEach(user ->
        {
            user.setPh(CountryToPhonePrefixUtil.prefixCode(user.getCountryCode()).concat(user.getPh()));
        });
        Assertions.assertEquals(expValue, users);
    }

    @Test
    void testGetUserById_Success() throws Exception {
        // Given
        User user = User.builder().id(7).name("Mike").countryCode("IN").address("New York").ph("+9188892290543").email("mike@test.com").build();
        User expValue = userRepository.save(user);
        // When
        mockMvc.perform(
                        get("/cms/user/{id}", expValue.getId())
                                .contentType(MediaType.APPLICATION_JSON))

                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expValue.getId()))
                .andExpect(jsonPath("$.name").value(expValue.getName()))
                .andExpect(jsonPath("$.address").value(expValue.getAddress()))
                .andExpect(jsonPath("$.countryCode").value(expValue.getCountryCode()))
                .andExpect(jsonPath("$.ph").value(expValue.getPh()))
                .andExpect(jsonPath("$.email").value(expValue.getEmail()));
    }

    @Test
    void testGetUserById_Failure() throws Exception {
        // Given
        User user = User.builder().id(7).name("Mike").countryCode("IN").address("New York").ph("8888892290543").email("mike@test.com").build();
        userRepository.save(user);

        // When
        mockMvc.perform(
                        get("/cms/user/{id}", 1000)
                                .contentType(MediaType.APPLICATION_JSON))

                // Then
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllUsers() throws Exception {
        // Given
        User userOne = User.builder().id(1).name("Mike").countryCode("IN").address("New York").ph("543218765").email("mike@test.com").build();
        User userTwo = User.builder().id(2).name("John").countryCode("IN").address("New York").ph("9876543").email("mike@test.com").build();
        List<User> user = new ArrayList<>();
        user.add(userOne);
        user.add(userTwo);
        userRepository.saveAll(user);

        // When
        mockMvc.perform(
                        get("/cms/users")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                //Then
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void testDeleteUser_Success() throws Exception {
        // Given
        User userOne = User.builder().id(1).name("Mike").countryCode("IN").address("New York").ph("543218765").email("mike@test.com").build();
        User user = userRepository.save(userOne);
        // When
        mockMvc.perform(
                        delete("/cms/user/delete/{id}", user.getId())
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Deleted")));
    }

    @Test
    void deleteUser_Failure() throws Exception {
        // Given
        int id = 100;
        // When
        mockMvc.perform(
                        delete("/cms/user/delete/{id}", id)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.message", is("Bad Request. User not found.")));
    }

    @Test
    void testFileUpload() throws Exception {
        // Given
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("file.xls");
        MockMultipartFile file = new MockMultipartFile("file", "file.xls", "application/vnd.ms-excel", inputStream);
        // When
        mockMvc.perform(
                        multipart("/cms/upload")
                                .file(file))
                // Then
                .andExpect(status().isCreated()).andReturn();
    }
}