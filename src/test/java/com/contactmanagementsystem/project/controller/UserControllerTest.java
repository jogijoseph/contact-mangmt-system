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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

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
    void addUser() throws Exception {
        // Given
        User user = User.builder().id(1).name("Mike").address("New York").countryCode("IN").ph("88888990543").email("mike@test.com").build();
        // When
        mockMvc.perform(
                        post("/cms/addUser")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(user)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)));
        //ToDo: need to check if data avail in DB
        Optional<User> actual=userRepository.findById(user.getId());
        user.setPh(CountryToPhonePrefixUtil.prefixCode(user.getCountryCode()).concat(user.getPh()));
        Assertions.assertEquals(user,actual.get());
    }

    @Test
    void addUsers() throws Exception {
        // Given
        User userOne = User.builder().name("Mike").address("New York").ph("543218765").email("mike@test.com").build();
        User userTwo = User.builder().name("John").address("New York").ph("9876543").email("mike@test.com").build();
        User userThree = User.builder().name("Adam").address("New York").ph("654345678").email("mike@test.com").build();
        List<User> users = new ArrayList<>();
        users.add(userOne);
        users.add(userTwo);
        users.add(userThree);
        // When
        mockMvc.perform(
                        post("/cms/addUsers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(users)))
                // Then
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)));
    }

    @Test
    void getUserById() throws Exception {
        // Given
        User user = User.builder().id(7).name("Mike").address("New York").ph("8888892290543").email("mike@test.com").build();
        User test = userRepository.save(user);
        // When
        mockMvc.perform(
                        get("/cms/user/{id}", test.getId())
                                .contentType(MediaType.APPLICATION_JSON))

                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(test.getId()))
                .andExpect(jsonPath("$.name").value(test.getName()))
                .andExpect(jsonPath("$.address").value(test.getAddress()))
                .andExpect(jsonPath("$.ph").value(test.getPh()))
                .andExpect(jsonPath("$.email").value(test.getEmail()));
    }

    @Test
    void getUserById_NotExist() throws Exception {
        // Given
        User user = User.builder().id(7).name("Mike").address("New York").ph("8888892290543").email("mike@test.com").build();
        User test = userRepository.save(user);
        // When
        mockMvc.perform(
                        get("/cms/user/{id}", 1000)
                                .contentType(MediaType.APPLICATION_JSON))

                // Then
                .andExpect(status().isNotFound());

    }

    @Test
    void getAllUsers() throws Exception {
        // Given
        User userOne = User.builder().id(5).name("Mike").address("New York").ph("54123218765").email("mike@test.com").build();
        User userTwo = User.builder().id(5).name("Mddike").address("New York").ph("54123444218765").email("mike@test.com").build();

        List<User> user = new ArrayList<>();
        user.add(userOne);
        user.add(userTwo);

         List<User> users=      userRepository.saveAll(user);
        // When
        MvcResult mvcResult = mockMvc.perform(
                        get("/cms/users")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                //Then
                .andExpect(status().isOk()).andReturn();
        //   .andExpect(jsonPath("$.[0].id").value(user.getId()))
               // .andExpect(jsonPath("$.[0].name").value(user.getName()))
              //  .andExpect(jsonPath("$.[0].address").value(user.getAddress()))
             ///   .andExpect(jsonPath("$.[0].ph").value(user.getPh()))
               // .andExpect(jsonPath("$.[0].email").value(user.getEmail()));
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void deleteUser() throws Exception {
        // Given
        User userOne = User.builder().id(5).name("Mike").address("New York").ph("54123218765").email("mike@test.com").build();
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
    void deleteUser_NotExist() throws Exception {
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
                .andExpect(jsonPath("$.message", is("Bad Request. Invalid user.")));
    }
}