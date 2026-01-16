package org.dgika.api.controller;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerTickerIT {

    @Autowired
    private MockMvc mockMvc;

    private String token;

    private static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:17")
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");

    private static final WireMockServer wireMockServer =
            new WireMockServer(wireMockConfig().dynamicPort());

    static {
        postgres.start();
        wireMockServer.start();
    }

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        registry.add("massive.api.base-url", wireMockServer::baseUrl);
        registry.add("massive.api.key", () -> "test-api-key");
    }

    @AfterAll
    void shutdown() {
        wireMockServer.stop();
        postgres.stop();
    }

    @BeforeAll
    void initData() throws Exception {
        register();
        token = login();
    }

    @BeforeEach
    void stubs() {
        wireMockServer.resetAll();

        wireMockServer.stubFor(get(urlPathMatching("/v2/aggs/ticker/[^/]+/range/1/day/[^/]+/[^/]+"))
                .withHeader("Authorization", matching("Bearer .*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                            {
                              "ticker":"AAPL",
                              "queryCount":2,
                              "resultsCount":2,
                              "status":"OK",
                              "results":[
                                {"t":1704067200000,"o":100.0,"c":101.0,"h":102.0,"l":99.0},
                                {"t":1704153600000,"o":101.0,"c":102.0,"h":103.0,"l":100.0}
                              ]
                            }
                        """)));
    }

    @Test
    void saveTicker() throws Exception {
        mockMvc.perform(post("/api/user/save")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                  "ticker":"AAPL",
                  "start":"2025-01-01",
                  "end":"2025-02-03"
                }
            """))
                .andExpect(status().isOk());
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
