package org.dgika.api.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestMethodOrder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class AuthControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    void shouldRegisterUser() throws Exception {

        mockMvc.perform(post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
            {
              "name": "TestTI",
              "email": "testTI@mail.com",
              "password": "12345678"
            }
        """))
                .andExpect(status().isCreated());

    }

    @Test
    @Order(2)
    void shouldReturnUserAlreadyExists() throws Exception {

        mockMvc.perform(post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
            {
              "name": "TestTI",
              "email": "testTI@mail.com",
              "password": "12345678"
            }
        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("User already exists"));

    }

    @Test
    @Order(3)
    void shouldReturnValidationFailed() throws Exception {

        mockMvc.perform(post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
            {
              "name": "T",
              "email": "testTImail.com",
              "password": "12345"
            }
        """))
                .andExpect(status().isBadRequest());

    }

}
