package org.dgika.api.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dgika.repository.PriceRepository;
import org.dgika.repository.TickerRepository;
import org.dgika.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SavedITTest {

    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private TickerRepository tickerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    private String token;
    private UUID userId;
    private UUID tickerId;

    private static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:17")
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");

    static {
        postgres.start();
    }

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @AfterAll
    void shutdown() {
        postgres.stop();
    }

    @BeforeAll
    void setUp() throws Exception {
        register();
        token = login();
        tickerRepository.insertIgnore("AAPL");
        tickerId = tickerRepository.findByName("AAPL").get().getId();
        userId = userRepository.findByEmail("testTI@mail.com").get().getId();
        priceRepository.insertPrice(
                userId,
                tickerId,
                LocalDate.of(2025, 1, 1),
                100.0, 101.0, 102.0, 99.0
        );

        priceRepository.insertPrice(
                userId,
                tickerId,
                LocalDate.of(2025, 1, 2),
                101.0, 102.0, 103.0, 100.0
        );

    }

    @Test
    @Order(1)
    void getSaved() throws Exception {
        mockMvc.perform(get("/api/user/saved")
                        .param("ticker", "AAPL")
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(org.springframework.test.web.servlet.result.MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.length()").value(2));
    }

    @Test
    @Order(2)
    void shouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/user/saved")
                        .param("ticker", "")
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private void register() throws Exception {
        mockMvc.perform(post("/api/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "name": "TestTI",
                      "email": "testTI@mail.com",
                      "password": "12345678"
                    }
                """)).andReturn();
    }

    private String login() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/api/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "email": "testTI@mail.com",
                      "password": "12345678"
                    }
                """)).andReturn();

        String json = mvcResult.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);

        return jsonNode.get("token").asText();
    }
}
