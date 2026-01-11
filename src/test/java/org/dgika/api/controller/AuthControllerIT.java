package org.dgika.api.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestMethodOrder;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
@Testcontainers
public class AuthControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Container
    private static final PostgreSQLContainer<?>
            postgres = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

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

    @Test
    @Order(4)
    void shouldReturnIsOk() throws Exception {

        mockMvc.perform(post("/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
            {
              "name": "TestTI",
              "email": "testTI@mail.com",
              "password": "12345678"
            }
        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isString())
                .andExpect(jsonPath("$.token").isNotEmpty());

    }

    @Test
    @Order(5)
    void shouldReturnBedRequestWithInvalidCredentials() throws Exception {

        mockMvc.perform(post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
            {
              "email": "testTImail.com",
              "password": "12345"
            }
        """))
                .andExpect(status().isBadRequest());

    }

}
